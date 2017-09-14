package holunda.taskassignment.plugin.dmn;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.plugin.TestApplication;
import holunda.taskassignment.plugin.process.GeneratorProcess;
import holunda.taskassignment.plugin.term.TermRepository;
import holunda.taskassignment.plugin.term.entity.TermEntity;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)

public class GeneratorProcessTest {

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TermRepository termRepository;

  @Autowired
  private RepositoryService repositoryService;

  @MockBean
  private BusinessDataService businessDataService;

  @Before
  public void setUp() throws Exception {
    termRepository.save(new TermEntity("all: weight>10 := foo"));
    termRepository.save(new TermEntity("box: length<=100 := bar"));
    termRepository.save(new TermEntity("sphere: radius>=10 := bar"));
  }

  @Test
  public void name() throws Exception {
    runtimeService.startProcessInstanceByKey(GeneratorProcess.PROCESS_KEY);

    List<DecisionDefinition> list = repositoryService.createDecisionDefinitionQuery().list();

    Assertions.assertThat(list).hasSize(2);
  }
}
