package de.holisticon.camunda.example.customquery.process.task;

import de.holisticon.camunda.example.customquery.model.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskWithOrder implements Serializable {

  @Id
  private String taskId;

  @Version
  private Integer version;

  private String name;

  private String description;

  private String taskDefinitionKey;

  @OneToOne
  private OrderEntity order;

  private Integer priority;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dueDate;

  @Temporal(TemporalType.TIMESTAMP)
  private Date followUpDate;

  private String processInstanceId;

  private String processDefinitionId;

  private String assignee;
}
