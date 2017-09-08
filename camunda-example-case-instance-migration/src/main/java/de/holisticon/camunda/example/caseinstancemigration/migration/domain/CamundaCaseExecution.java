package de.holisticon.camunda.example.caseinstancemigration.migration.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Getter
@Entity
@Table(name = "ACT_RU_CASE_EXECUTION")
public class CamundaCaseExecution {

    @Id
    @Column(name = "ID_")
    private String id;

    @Version
    @Column(name = "REV_")
    private Integer revision;

    @Column(name = "CASE_INST_ID_")
    private String caseInstanceId;

    @Column(name = "SUPER_CASE_EXEC_")
    private String superCaseExecutionId;

    @Column(name = "SUPER_EXEC_")
    private String superExecutionId;

    @Column(name = "BUSINESS_KEY_")
    private String businessKey;

    @Column(name = "PARENT_ID_")
    private String parentId;

    @Setter
    @Column(name = "CASE_DEF_ID_")
    private String caseDefinitionId;

    @Column(name = "ACT_ID_")
    private String activityId;

    @Column(name = "PREV_STATE_")
    private Integer previousState;

    @Column(name = "CURRENT_STATE_")
    private Integer currentState;

    @Column(name = "REQUIRED_")
    private Boolean required;

    @Column(name = "TENANT_ID_")
    private String tenantId;

    private CamundaCaseExecution(){} //NOSONAR
}
