package com.compomics.util.experiment.identification.matches;

import com.compomics.util.db.object.ObjectsDB;
import com.compomics.util.experiment.personalization.ExperimentObject;

/**
 * This class models the match between theoretic PTM and identification results.
 *
 * @author Marc Vaudel
 * @author Dominik Kopczynski
 */
public class ModificationMatch extends ExperimentObject {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = 7129515983284796207L;
    /**
     * The modification name. The modification can be accessed via
     * the factory.
     */
    private String modification;
    /**
     * Is the modification variable?
     */
    private boolean variable;
    /**
     * The location in the sequence, 1 is the first residue.
     */
    private int modifiedSite;
    /**
     * A boolean indicating whether the modification is confidently localized
     * onto the sequence. Not applicable to fixed or terminal modifications.
     */
    private boolean confident = false;
    /**
     * A boolean indicating whether the modification is inferred from another
     * peptide. Not applicable to fixed or terminal modifications.
     */
    private boolean inferred = false;

    /**
     * Constructor for a modification match.
     *
     * @param theoreticPtm the theoretic PTM
     * @param variable true for variable modifications, false otherwise
     * @param modifiedSite the position of the modification in the sequence, 1
     * is the first residue
     */
    public ModificationMatch(String theoreticPtm, boolean variable, int modifiedSite) {
        
        this.modification = theoreticPtm;
        this.variable = variable;
        this.modifiedSite = modifiedSite;
        
    }

    /**
     * Default constructor for a modification match.
     */
    public ModificationMatch() {
    }

    /**
     * Returns a boolean indicating if the modification is variable.
     *
     * @return a boolean indicating if the modification is variable
     */
    public boolean getVariable() {
        
        ObjectsDB.increaseRWCounter(); zooActivateRead(); ObjectsDB.decreaseRWCounter();
        
        return variable;
    }

    /**
     * Getter for the theoretic PTM name.
     *
     * @return the theoretic PTM name
     */
    public String getModification() {
        
        ObjectsDB.increaseRWCounter(); zooActivateRead(); ObjectsDB.decreaseRWCounter();
        
        return modification;
    }

    /**
     * Sets the theoretic PTM.
     *
     * @param ptm the theoretic PTM name
     */
    public void setModification(String ptm) {
        
        ObjectsDB.increaseRWCounter(); zooActivateWrite(); ObjectsDB.decreaseRWCounter();
        
        this.modification = ptm;
    }

    /**
     * Getter for the modification site, 1 is the first amino acid.
     *
     * @return the index of the modification in the sequence
     */
    public int getModificationSite() {
        
        ObjectsDB.increaseRWCounter(); zooActivateRead(); ObjectsDB.decreaseRWCounter();
        
        return modifiedSite;
    }

    /**
     * Setter for the modification site, 1 is the first amino acid.
     *
     * @param site the index of the modification in the sequence
     */
    public void setModificationSite(int site) {
        
        ObjectsDB.increaseRWCounter(); zooActivateWrite(); ObjectsDB.decreaseRWCounter();
        
        this.modifiedSite = site;
    }

    /**
     * Returns a boolean indicating whether the modification is confidently
     * localized on the sequence.
     *
     * @return a boolean indicating whether the modification is confidently
     * localized on the sequence
     */
    public boolean getConfident() {
        
        ObjectsDB.increaseRWCounter(); zooActivateRead(); ObjectsDB.decreaseRWCounter();
        
        return confident;
    }

    /**
     * Sets whether the modification is confidently localized on the sequence.
     *
     * @param confident a boolean indicating whether the modification is
     * confidently localized on the sequence
     */
    public void setConfident(boolean confident) {
        
        ObjectsDB.increaseRWCounter(); zooActivateWrite(); ObjectsDB.decreaseRWCounter();
        
        this.confident = confident;
    }

    /**
     * Returns a boolean indicating whether the modification is inferred from
     * another peptide.
     *
     * @return a boolean indicating whether the modification is inferred from
     * another peptide
     */
    public boolean getInferred() {
        
        ObjectsDB.increaseRWCounter(); zooActivateRead(); ObjectsDB.decreaseRWCounter();
        
        return inferred;
    }

    /**
     * Sets whether the modification is inferred from another peptide.
     *
     * @param inferred a boolean indicating whether the modification is inferred
     * from another peptide
     */
    public void setInferred(boolean inferred) {
        
        ObjectsDB.increaseRWCounter(); zooActivateWrite(); ObjectsDB.decreaseRWCounter();
        
        this.inferred = inferred;
    }
    
    public void setVariable(boolean variable){
        
        ObjectsDB.increaseRWCounter(); zooActivateWrite(); ObjectsDB.decreaseRWCounter();
        
        this.variable = variable;
    }

    /**
     * Indicates whether this modification match is the same of another one. The
     * match is only compared based on the theoretic PTM and the variability.
     * The localization and its confidence is not taken into account.
     *
     * @param anotherModificationMatch another modification match
     *
     * @return a boolean indicating whether both modification matches are the
     * same.
     */
    public boolean isSameAs(ModificationMatch anotherModificationMatch) {
        
        ObjectsDB.increaseRWCounter(); zooActivateRead(); ObjectsDB.decreaseRWCounter();
        
        if (!modification.equals(anotherModificationMatch.getModification())) {
            return false;
        }
        return variable == anotherModificationMatch.getVariable();
    }
    
    /**
     * Clones the modification match into a new match with the same attributes.
     * 
     * @return a new modification match with the same attributes
     */
    public ModificationMatch clone() {
        
        ObjectsDB.increaseRWCounter(); zooActivateRead(); ObjectsDB.decreaseRWCounter();
        
        ModificationMatch newMatch = new ModificationMatch(modification, variable, modifiedSite);
        newMatch.setConfident(confident);
        newMatch.setInferred(inferred);
        
        return newMatch;
    }
}
