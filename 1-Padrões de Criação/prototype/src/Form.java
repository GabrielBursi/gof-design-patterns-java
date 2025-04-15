import java.util.ArrayList;
import java.util.List;

public class Form implements Prototype {
    List<Field> fields;
    String formId;
    String category;
    String description;

    public Form(String formId, String category, String description) {
        this.formId = formId;
        this.category = category;
        this.description = description;
        this.fields = new ArrayList<Field>();
    }

    public Form clone () {
        Form newForm = new Form(this.formId, this.category, this.description);
        List<Field> fields = new ArrayList<>();
        this.fields.stream().forEach(f -> fields.add(f.clone()));
        newForm.fields = fields;
        return newForm;
    }

    void addField (String type, String title) {
		this.fields.add(Field.create(type, title));
	}
}
