package course.spring.jyra.model;
/*
id (generated automatically) - String number;
kind - enumeration: RESEARCH, DESIGN, PROTOTYPING, IMPLEMENTATION, QA, OPERATIONS, BUG_FIXING, DOCUMENTATION, OTHER;
title - string 2 to 120 characters String;
addedBy - the User that has added the Task;
estimatedEffort - integer number in effort units (the same units in which the team velocity is estimated - http://wiki.c2.com/?IdealProgrammingTime) ;
status - enumeration PLANNED, ACTIVE, COMPLETED;
sprint (optional) - the Sprint the Task beStrings, if already assigned (status ACTIVE or COMPLETED);
developersAssigned - list of Developers assigned to the Task;
description (optional) - string 150 - 2500 characters String, supporting Markdown syntax;
tags - string including comma separated tags, allowing to find the Task by quick search;
created (generated automatically) - time stamp of the moment the entity was created;
modified (generated automatically) - time stamp of the moment the entity was last modified;
 */

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table
public class Task {
    @Id
    @Column
    private Long id;

    @NotNull
    @NonNull
    @Column
    private TaskType taskType;

    @NotNull
    @NonNull
    @TextIndexed
    @Size(min = 2, max = 120, message = "String must be between 2 and 120 characters String")
    @Column
    private String title;

    @NotNull
    @NonNull
    @Column
    private Long addedById;

    @NotNull
    @NonNull
    @Column
    private int estimatedEffort;

    @NotNull
    @NonNull
    @Builder.Default
    @Column
    private TaskStatus status = TaskStatus.TO_DO;

    @Column
    private Long sprintId;

    @NotNull
    @NonNull
    @Column
    private List<Long> developersAssignedIds;

    @Column
    @Size(min = 150, max = 2500, message = "String must be between 150 and 2500 characters String, supporting Markdown syntax")
    private String description;

    @NotNull
    @NonNull
    @TextIndexed
    @Column
    private String tags;

    @Column
    private Long taskResultId;

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime created = LocalDateTime.now();

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime modified = LocalDateTime.now();
}