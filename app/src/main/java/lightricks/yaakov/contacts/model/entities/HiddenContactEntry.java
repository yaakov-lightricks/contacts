package lightricks.yaakov.contacts.model.entities;

/*
    wrapper class for contactEntry representing current hidden status if ContactEntry
 */
public class HiddenContactEntry {
    private final ContactEntry entry;
    private final boolean isHidden;

    public HiddenContactEntry(ContactEntry entry, boolean isHidden) {
        this.entry = entry;
        this.isHidden = isHidden;
    }

    public ContactEntry getEntry() {
        return entry;
    }

    public boolean isHidden() {
        return isHidden;
    }
}
