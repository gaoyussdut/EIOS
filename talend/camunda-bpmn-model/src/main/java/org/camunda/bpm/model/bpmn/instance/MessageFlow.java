/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.model.bpmn.instance;

import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;

/**
 * The BPMN messageFlow element
 *
 * @author Sebastian Menski
 */
public interface MessageFlow extends BaseElement {

  String getName();

  void setName(String name);

  InteractionNode getSource();

  void setSource(InteractionNode source);

  InteractionNode getTarget();

  void setTarget(InteractionNode target);

  Message getMessage();

  void setMessage(Message message);

  BpmnEdge getDiagramElement();

}
