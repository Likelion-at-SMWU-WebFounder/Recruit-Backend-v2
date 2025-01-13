package com.smlikelion.webfounder.admin;

import com.smlikelion.webfounder.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuperUserInitializer implements ApplicationRunner {

    private final AdminService adminService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
       adminService.createSuperUser();
    }
}