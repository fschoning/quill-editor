package org.vaadin.schoning.quill;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.shared.Registration;


public interface QuillValueChangeNotifier {

    default Registration addHtmlValueChangeListener(
            ComponentEventListener<QuillHtmlValueChangeEvent> listener) {
        if (this instanceof Component) {
            return ComponentUtil.addListener((Component) this, QuillHtmlValueChangeEvent.class,  listener);
        } else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addValueChangeListener"));
        }
    }

    default Registration addDeltaValueChangeListener(
            ComponentEventListener<QuillDeltaValueChangeEvent> listener) {
        if (this instanceof Component) {
            return ComponentUtil.addListener((Component) this, QuillDeltaValueChangeEvent.class,  listener);
        } else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addValueChangeListener"));
        }
    }

    class QuillDeltaValueChangeEvent extends ComponentEvent<Component> {

        private final String value;

        public QuillDeltaValueChangeEvent(Component source, String deltaValue) {
            super(source, true);
            this.value = deltaValue;
        }

        public String getValue() {
            return value;
        }
    }

    class QuillHtmlValueChangeEvent extends ComponentEvent<Component> {

        private final String value;

        public QuillHtmlValueChangeEvent(Component source, String htmlValue) {
            super(source, true);
            this.value = htmlValue;
        }

        public String getValue() {
            return value;
        }
    }
}

