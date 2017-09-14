package holunda.taskassignment.plugin.dmn;

import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.impl.DmnModelConstants;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.model.dmn.instance.DecisionTable;
import org.camunda.bpm.model.dmn.instance.Definitions;
import org.camunda.bpm.model.dmn.instance.DmnElement;
import org.camunda.bpm.model.dmn.instance.Input;
import org.camunda.bpm.model.dmn.instance.InputEntry;
import org.camunda.bpm.model.dmn.instance.InputExpression;
import org.camunda.bpm.model.dmn.instance.NamedElement;
import org.camunda.bpm.model.dmn.instance.OutputEntry;
import org.camunda.bpm.model.dmn.instance.Rule;
import org.camunda.bpm.model.dmn.instance.Text;

import java.util.ArrayList;
import java.util.List;

import static org.camunda.bpm.model.dmn.impl.DmnModelConstants.DMN_ELEMENT_DECISION_TABLE;
import static org.camunda.bpm.model.dmn.impl.DmnModelConstants.DMN_ELEMENT_DEFINITIONS;

/**
 * Utility class to simplify usage of DmnModelApi.
 */
public final class DmnUtils {
  public static DmnModelInstance initializeEmptyDmnModel() {
    final DmnModelInstance dmnModel = Dmn.createEmptyModel();
    final Definitions definitions = generateNamedElement(dmnModel, Definitions.class, DMN_ELEMENT_DEFINITIONS);
    definitions.setNamespace(DmnModelConstants.CAMUNDA_NS);
    dmnModel.setDefinitions(definitions);

    return dmnModel;
  }

  public static <E extends NamedElement> E generateNamedElement(DmnModelInstance modelInstance, Class<E> elementClass, String name) {
    E element = generateElement(modelInstance, elementClass, name);
    element.setName(name);
    return element;
  }

  public static <E extends DmnElement> E generateElement(DmnModelInstance modelInstance, Class<E> elementClass, String id) {
    E element = modelInstance.newInstance(elementClass);
    element.setId(id);
    return element;
  }

  public static <E extends DmnElement> E generateElement(DmnModelInstance modelInstance, Class<E> elementClass) {
    String generatedId = elementClass.getSimpleName() + Integer.toString((int) (2.147483647E9D * Math.random()));
    return generateElement(modelInstance, elementClass, generatedId);
  }

  public static Text generateText(DmnModelInstance modelInstance, String content) {
    Text text = modelInstance.newInstance(Text.class);
    text.setTextContent(content);
    return text;
  }

  private DmnUtils() {
    // util class, do not instantiate
  }
}
