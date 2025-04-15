import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FormTest {

    @Test
    public void testCopyForm() {
        FormRepositoryMemory formRepository = new FormRepositoryMemory();
        Form form = new Form("1", "Marketing", "Leads v1");
        form.addField("text", "name");
        form.addField("text", "email");
        formRepository.save(form);
        CopyForm copyForm = new CopyForm(formRepository);
        CopyFormInputDTO input = new CopyFormInputDTO("1", "2", "test", "junit java");
        copyForm.execute(input);
        Form newForm = formRepository.getById("2");
        assertEquals("test", newForm.category);
        assertEquals("junit java", newForm.description);
        assertEquals(2, newForm.fields.size());
        assertEquals("name", newForm.fields.get(0).title);
        assertEquals("email", newForm.fields.get(1).title);
    }
}
