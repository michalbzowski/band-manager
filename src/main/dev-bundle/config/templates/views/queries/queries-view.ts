import '@vaadin/button';
import '@vaadin/horizontal-layout';
import '@vaadin/text-field';
import { html, LitElement } from 'lit';
import { customElement } from 'lit/decorators.js';

@customElement('queries-view')
export class QueriesView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`<vaadin-horizontal-layout theme="margin spacing" class="items-end"
      ><vaadin-text-field id="name" label="Your name"></vaadin-text-field>
      <vaadin-button id="sayHello">Say hello</vaadin-button></vaadin-horizontal-layout
    >`;
  }
}
