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

    @Setter
    @Column(name = "CASE_DEF_ID_")
    private String caseDefinitionId;

    private CamundaTask(){} //NOSONAR
}
