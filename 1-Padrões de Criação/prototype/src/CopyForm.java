public class CopyForm {
    private FormRepository formRepository;

    public CopyForm (FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    void execute (CopyFormInputDTO input) {
        Form form = this.formRepository.getById(input.fromFormId());
        Form newForm = form.clone();
        newForm.formId = input.newFormId();
		newForm.category = input.newCategory();
		newForm.description = input.newDescription();
        this.formRepository.save(newForm);
    }
}
