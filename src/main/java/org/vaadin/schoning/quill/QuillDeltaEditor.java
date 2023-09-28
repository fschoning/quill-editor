package org.vaadin.schoning.quill;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import static org.vaadin.schoning.quill.QuillEditorComponent.EMPTY_DELTA_VALUE;

public class QuillDeltaEditor extends AbstractCompositeField<Div, QuillDeltaEditor, String> implements HasValidation, HasSize {
    private Span errorMessageLabel = new Span();
    private boolean invalid;

    private QuillEditorComponent quillEditorComponent = new QuillEditorComponent();

    public QuillDeltaEditor() {
        super(EMPTY_DELTA_VALUE);

        getContent().add(errorMessageLabel, quillEditorComponent);
        quillEditorComponent.addDeltaValueChangeListener(quillValueChangeEvent -> {
            setValue(quillValueChangeEvent.getValue());
        });

        getContent().addClassName("quill-editor");
        errorMessageLabel.addClassName("error-message");
        errorMessageLabel.setVisible(false);
        quillEditorComponent.setWidth("100%");
        quillEditorComponent.setHeight("90%");

        // Hack to ensure content displays on page refresh or when component is opened in a dialog and the dialog is closed and then opened again
        /* Not sure if this is necessary for Delta editor? quillEditorComponent.addAttachListener(e -> {
            getToolbarConfigurator().initEditor();
            quillEditorComponent.getElement().getNode().runWhenAttached(ui -> {
                quillEditorComponent.getElement().executeJs("$0.setContent($1)", quillEditorComponent.getElement(),  getValue());
            });
        });*/

    }

    @Override
    protected void setPresentationValue(String newPresentationValue) {
        _setValueBasedOnReadOnly(newPresentationValue, isReadOnly());
    }

    /* Overriding this does not seem to work - editor comes up blank
    @Override
    public String getValue(){
        return quillEditorComponent.getDeltaContent();
    }
     */

    @Override
    public void clear() {
        super.clear();
        quillEditorComponent.setDeltaContent("");
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        errorMessageLabel.setText(errorMessage);
    }

    @Override
    public String getErrorMessage() {
        return errorMessageLabel.getText();
    }

    @Override
    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
        errorMessageLabel.setVisible(invalid);
        if(errorMessageLabel.isVisible()){
            quillEditorComponent.setHeight("80%");
        }else {
            quillEditorComponent.setHeight("90%");
        }
    }

    @Override
    public boolean isInvalid() {
        return invalid;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        _setValueBasedOnReadOnly(getValue(), readOnly);

    }

    private void _setValueBasedOnReadOnly(String value, boolean isReadOnly){
        if(isReadOnly){
            quillEditorComponent.setDeltaContent(getValue());
            // TODO - set the editor to readonly
        }else {
            quillEditorComponent.setDeltaContent(getValue());
        }
    }

    public QuillToolbarConfigurator getToolbarConfigurator(){
        return quillEditorComponent;
    }

    public void initEditor() {
        quillEditorComponent.initEditor();
    }
}
