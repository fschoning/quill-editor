package org.vaadin.schoning.quill;

import java.util.Objects;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.DisabledUpdateMode;
import com.vaadin.flow.function.SerializableConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom RichText editor component for Flow using Quill library.
 */
@Tag("quill-editor")
@NpmPackage(value = "lit-element", version = "^3.0.0")
@NpmPackage(value = "lit-html", version = "^2.4.0")
@NpmPackage(value = "quill", version = "^1.3.7")
@JsModule("./quilleditor.js")
@CssImport("./quill.snow.css")
@CssImport("./custom-quillEditor.css")
public class QuillEditorComponent extends Component implements HasComponents, QuillToolbarConfigurator, HasSize, QuillValueChangeNotifier, HasStyle {

    private static Logger log = LoggerFactory.getLogger(QuillEditorComponent.class);

    public static final String EMPTY_HTML_VALUE = "<p><br></p>";
    public static final String EMPTY_DELTA_VALUE = "";

    private Div editor = new Div();
    private String htmlContent = "";
    private String deltaContent = "";

    public QuillEditorComponent() {
        initEditor();
    }

    /**
     * Initialize the frontend editor component applying all toolbar configuration
     * done through the toolbar properties assignable through the interface {@link QuillToolbarConfigurator}.
     * If no property have been configured through the {@link QuillToolbarConfigurator}
     * methods, the editor will be initialized including all the features of the toolbar.
     */
    public void initEditor(){
        this.removeAll();
        editor = new Div();
        editor.setId("editor-quill");
        add(editor);
        this.getElement().executeJs("$0.initEditor($1)",this, editor.getElement());
    }

    @ClientCallable(DisabledUpdateMode.ALWAYS)
    private void setHtml(String htmlContent) {
        //log.debug("setHtml: htmlContent={}", htmlContent);
        final String noNewLineCharacter = htmlContent.replaceAll("\n", "");
        final String oldContent = this.htmlContent;
        if(!Objects.equals(oldContent, noNewLineCharacter)){
            this.htmlContent = noNewLineCharacter;
            this.fireEvent(new QuillValueChangeNotifier.QuillHtmlValueChangeEvent(this, noNewLineCharacter));
        }
    }

    @ClientCallable(DisabledUpdateMode.ALWAYS)
    private void setContent(String deltaContent) {
        //log.debug("setContent: deltaContent={}", deltaContent);
        final String oldContent = this.deltaContent;
        if(!Objects.equals(oldContent, deltaContent)){
            this.deltaContent = deltaContent;
            this.fireEvent(new QuillValueChangeNotifier.QuillDeltaValueChangeEvent(this, deltaContent));
        }
    }

    /**
     * Returns the editor's content in HTML format.
     *
     * @return a {@link String} instance of the current editor content
     *      in HTML format.
     */
    public String getHtmlContent(){
        return htmlContent;
    }

    /**
     * Sets the editor content as an HTML format. Any unsupported
     * HTML content will be ignored and removed from the editor.
     *
     * @param htmlContent a {@link String} instance of the content in HTML format
     *                   that should appear on the editor.
     */
    public void setHtmlContent(String htmlContent){
        final String oldContent = this.htmlContent;
        if(!Objects.equals(oldContent, htmlContent)){
            this.htmlContent = htmlContent;
            runBeforeClientResponse(ui -> {
                editor.getElement().executeJs("$0.setHtml($1)", this,  htmlContent);
            });
        }
    }

    public String getDeltaContent(){
        return deltaContent;
    }

    public void setDeltaContent(String deltaContent){
        final String oldContent = this.deltaContent;
        if(!Objects.equals(oldContent, deltaContent)){
            this.deltaContent = deltaContent;
            runBeforeClientResponse(ui -> {
                editor.getElement().executeJs("$0.setContent($1)", this,  deltaContent);
            });
        }
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

}