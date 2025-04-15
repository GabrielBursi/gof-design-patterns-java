import java.util.ArrayList;
import java.util.List;

public class FormRepositoryMemory implements FormRepository {
    private List<Form> forms;

    public FormRepositoryMemory() {
        this.forms = new ArrayList<>();
    }

    public Form getById(String formId) {
        return this.forms.stream().filter(f -> f.formId.equals(formId)).findFirst().orElse(null);
    }

    public void save(Form form) {
        this.forms.add(form);
    }

}
