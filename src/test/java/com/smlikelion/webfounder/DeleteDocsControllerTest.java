package com.smlikelion.webfounder;

import com.smlikelion.webfounder.admin.entity.Role;
import com.smlikelion.webfounder.admin.exception.UnauthorizedRoleException;
import com.smlikelion.webfounder.global.exception.GlobalExceptionHandler; // adjust if package differs
import com.smlikelion.webfounder.manage.controller.ManageController;
import com.smlikelion.webfounder.manage.dto.response.DeleteDocsResponse;
import com.smlikelion.webfounder.manage.service.ManageService;
import com.smlikelion.webfounder.manage.service.SQLExecutionService;
import com.smlikelion.webfounder.security.Auth;
import com.smlikelion.webfounder.security.AuthInfo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Focused tests for /api/manage/apply/docs/delete
 * - Success: 200 OK + service invoked with expected args
 * - Forbidden: 403
 * - Validation: 400
 * (No JSON structure assertions -> resilient to wrapper/field name differences)
 */
class DeleteDocsControllerTest {

    /** Minimal @Auth resolver stub */
    private static class TestAuthResolver implements HandlerMethodArgumentResolver {
        private final List<Role> roles;
        TestAuthResolver(List<Role> roles) { this.roles = roles; }

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(Auth.class)
                    && parameter.getParameterType().isAssignableFrom(AuthInfo.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter,
                                      ModelAndViewContainer mavContainer,
                                      org.springframework.web.context.request.NativeWebRequest webRequest,
                                      org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
            AuthInfo auth = Mockito.mock(AuthInfo.class);
            when(auth.getRoles()).thenReturn(roles);
            return auth;
        }
    }

    private ManageController buildController(SQLExecutionService sqlServiceMock) {
        ManageService manageService = Mockito.mock(ManageService.class);
        return new ManageController(manageService, null, sqlServiceMock);
    }

    @Test
    void deleteSelectedDocs_success_200_and_serviceCalledWithIds() throws Exception {
        SQLExecutionService sqlService = Mockito.mock(SQLExecutionService.class);
        ManageController controller = buildController(sqlService);

        when(sqlService.deleteDocsByJoinerIds(any(AuthInfo.class), any()))
                .thenReturn(DeleteDocsResponse.builder()
                        .requested(3).deleted(2).failed(List.of(999L)).build());

        MockMvc mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new TestAuthResolver(List.of(Role.SUPERUSER)))
                .build();

        String body = "{\"joinerIds\":[1,2,999]}";

        mockMvc.perform(post("/api/manage/apply/docs/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        // verify service was called with expected list
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Long>> idsCaptor = ArgumentCaptor.forClass(List.class);
        verify(sqlService, times(1))
                .deleteDocsByJoinerIds(any(AuthInfo.class), idsCaptor.capture());

        assertThat(idsCaptor.getValue()).containsExactly(1L, 2L, 999L);
    }

    @Test
    void deleteSelectedDocs_forbidden_403_when_role_is_user() throws Exception {
        SQLExecutionService sqlService = Mockito.mock(SQLExecutionService.class);
        ManageController controller = buildController(sqlService);

        when(sqlService.deleteDocsByJoinerIds(any(AuthInfo.class), any()))
                .thenThrow(new UnauthorizedRoleException("Access denied"));

        MockMvc mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new TestAuthResolver(List.of(Role.USER))) // insufficient
                .build();

        String body = "{\"joinerIds\":[1,2]}";

        mockMvc.perform(post("/api/manage/apply/docs/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteSelectedDocs_validation_400_when_missing_or_empty() throws Exception {
        SQLExecutionService sqlService = Mockito.mock(SQLExecutionService.class);
        ManageController controller = buildController(sqlService);

        MockMvc mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new TestAuthResolver(List.of(Role.SUPERUSER)))
                .build();

        String bodyMissing = "{}";
        String bodyEmpty = "{\"joinerIds\":[]}";

        mockMvc.perform(post("/api/manage/apply/docs/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyMissing))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/manage/apply/docs/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyEmpty))
                .andExpect(status().isBadRequest());
    }
}
