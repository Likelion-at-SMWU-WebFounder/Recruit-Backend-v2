package com.smlikelion.webfounder.Recruit.Entity;

import com.smlikelion.webfounder.global.entity.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "mail")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mail extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_id")
    private Long mailId;

    @Column(name = "email_address", nullable = false)
    private String emailAdd;


}
