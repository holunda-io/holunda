package de.holisticon.camunda.example.caseinstancemigration.migration.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

@Getter
@Entity
@Table(name = "ACT_RU_TASK")
public class CamundaTask {

    @Id
    @Column(name = "ID_")
    private String id;

    @Version
    @Column(name = "REV_")
    private Integer revision;

    @Column(name = "EXECUTION_ID_")
    private String executionId;

    @Column(name = "PROC_INST_ID_")
    private String processInstanceId;

    @Column(name = "PROC_DEF_ID_")
    private String processDefinitionId;

    @Column(name = "CASE_EXECUTION_ID_")

    private String caseExecutionId;

    @Column(name = "CASE_INST_ID_")
    private String caseInstanceId;

    @Setter
    @Column(name = "CASE_DEF_ID_")
    private String caseDefinitionId;

    @Column(name = "NAME_")
    private String name;

    @Column(name = "PARENT_TASK_ID_")
    private String parentTaskId;

    @Column(name = "DESCRIPTION_")
    private String description;

    @Column(name = "TASK_DEF_KEY_")
    private String taskDefinitionKey;

    @Column(name = "OWNER_")
    private String owner;

    @Column(name = "ASSIGNEE_")
    private String assignee;

    @Column(name = "DELEGATION_")
    private String delegation;

    @Column(name = "PRIORITY_")
    private Integer priority;

    @Column(name = "CREATE_TIME_")
    private Date createTime;

    @Column(name = "DUE_DATE_")
    private Date dueDate;

    @Column(name = "FOLLOW_UP_DATE_")
    private Date followUpDate;

    @Column(name = "SUSPENSION_STATE_")
    private Integer suspensionState;

    @Column(name = "TENANT_ID_")
    private String tenantId;

    private CamundaTask(){} //NOSONAR
}
