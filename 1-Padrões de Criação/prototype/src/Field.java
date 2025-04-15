import java.util.UUID;

public class Field implements Prototype {
    final String fieldId;
    final String type;
    final String title;

    public Field(String fieldId, String type, String title) {
        this.fieldId = fieldId;
        this.type = type;
        this.title = title;
    }

    static Field create(String type, String title) {
        String fieldId = UUID.randomUUID().toString();
        return new Field(fieldId, type, title);
    }

    public Field clone() {
        return new Field(this.fieldId, this.type, this.title);
    }
}
