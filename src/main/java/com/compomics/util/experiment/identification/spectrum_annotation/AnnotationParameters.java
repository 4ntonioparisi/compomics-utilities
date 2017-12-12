package com.compomics.util.experiment.identification.spectrum_annotation;

import com.compomics.util.experiment.biology.ions.Ion;
import com.compomics.util.experiment.biology.ions.IonFactory;
import com.compomics.util.experiment.biology.ions.NeutralLoss;
import com.compomics.util.experiment.biology.ions.impl.PeptideFragmentIon;
import com.compomics.util.experiment.biology.ions.impl.ReporterIon;
import com.compomics.util.parameters.identification.search.SearchParameters;
import com.compomics.util.experiment.identification.SpectrumIdentificationAssumption;
import com.compomics.util.parameters.identification.search.ModificationParameters;
import com.compomics.util.parameters.identification.advanced.SequenceMatchingParameters;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * This class contains the spectrum annotation preferences.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class AnnotationParameters implements Serializable {

    /**
     * Enum of the types of intensity thresholds available.
     */
    public enum IntensityThresholdType {

        percentile("Percentile of the intensities."),
        snp("Signal to noise probability.");

        /**
         * The description of the thresold.
         */
        public final String description;

        /**
         * Constructor.
         *
         * @param description the description of the thresold
         */
        private IntensityThresholdType(String description) {
            this.description = description;
        }
    }

    /**
     * Serial number for backward compatibility.
     */
    static final long serialVersionUID = -3739182405876385430L;
    /**
     * If true, the automatic y-axis zoom excludes background peaks. False
     * includes all peaks in the auto zoom.
     */
    private boolean yAxisZoomExcludesBackgroundPeaks = true;
    /**
     * If true, all peaks are shown, false displays the annotated peaks, and the
     * non-annotated in the background.
     */
    private boolean showAllPeaks = false;
    /**
     * The type of intensity threshold
     */
    private IntensityThresholdType intensityThresholdType = IntensityThresholdType.percentile;
    /**
     * The intensity thresold to use. The type of threshold is defined according
     * to the IntensityThreshold enum.
     */
    private double intensityLimit = 0.75;
    /**
     * Shall automatic annotation be used.
     */
    private boolean automaticAnnotation = true;
    /**
     * The types of ions to annotate.
     */
    private HashMap<Ion.IonType, HashSet<Integer>> selectedIonsMap = new HashMap<>(4);
    /**
     * List of neutral losses to annotate.
     */
    private ArrayList<NeutralLoss> neutralLossesList;
    /**
     * If true neutral losses will be automatically deduced from the spectrum
     * identification assumption.
     */
    private Boolean neutralLossesAuto = true;
    /**
     * If true reporter ions will be annotated by default.
     */
    private Boolean reporterIons = true;
    /**
     * If true related ions will be annotated by default.
     */
    private Boolean relatedIons = true;
    /**
     * Fragment ion accuracy used for peak matching.
     */
    private double fragmentIonAccuracy;
    /**
     * Indicates whether the fragment ion accuracy is in ppm.
     */
    private Boolean fragmentIonPpm = false;
    /**
     * If true, the automatic forward ion de novo tags are shown.
     */
    private boolean showForwardIonDeNovoTags = false;
    /**
     * If true, the automatic rewind ion de novo tags are shown.
     */
    private boolean showRewindIonDeNovoTags = false;
    /**
     * The charge for the fragment ions in the de novo sequencing.
     */
    private int deNovoCharge = 1;
    /**
     * If there are more than one matching peak for a given annotation setting
     * this value to true results in the most accurate peak being annotated,
     * while setting this to false annotates the most intense peak.
     *
     * @deprecated use tiesResolution instead
     */
    private Boolean highResolutionAnnotation = true;
    /**
     * The method to use for resolution of ties when multiple peaks can be
     * assigned to a fragment.
     */
    private SpectrumAnnotator.TiesResolution tiesResolution = SpectrumAnnotator.TiesResolution.mostAccurateMz;

    /**
     * Constructor.
     */
    public AnnotationParameters() {
    }

    /**
     * Returns the annotation preferences specific to a spectrum and an
     * identification assumption.
     *
     * @param spectrumKey the key of the spectrum to annotate
     * @param spectrumIdentificationAssumption the spectrum identification
     * assumption to annotate with
     * @param sequenceMatchingPreferences the sequence matching preferences for
     * peptide to protein mapping
     * @param ptmSequenceMatchingPreferences the sequence matching preferences
     * for PTM to peptide mapping
     *
     * @return the annotation preferences specific to a spectrum and an
     * identification assumption
     */
    public SpecificAnnotationParameters getSpecificAnnotationPreferences(String spectrumKey, SpectrumIdentificationAssumption spectrumIdentificationAssumption,
            SequenceMatchingParameters sequenceMatchingPreferences, SequenceMatchingParameters ptmSequenceMatchingPreferences) {

        SpecificAnnotationParameters specificAnnotationPreferences = new SpecificAnnotationParameters(spectrumKey, spectrumIdentificationAssumption);
        specificAnnotationPreferences.setNeutralLossesAuto(neutralLossesAuto);
        
        if (neutralLossesAuto) {
            
            specificAnnotationPreferences.setNeutralLossesMap(SpectrumAnnotator.getDefaultLosses(spectrumIdentificationAssumption, sequenceMatchingPreferences, ptmSequenceMatchingPreferences));
        
        } else {
        
            NeutralLossesMap tempNeutralLossesMap = new NeutralLossesMap();
            
            for (NeutralLoss neutralLoss : getNeutralLosses()) {
            
                tempNeutralLossesMap.addNeutralLoss(neutralLoss, 1, 1);
            
            }
            
            specificAnnotationPreferences.setNeutralLossesMap(tempNeutralLossesMap);
        
        }
        
        ArrayList<Integer> charges = new ArrayList<>(4);
        int precursorCharge = spectrumIdentificationAssumption.getIdentificationCharge();
        
        if (precursorCharge == 1) {
        
            charges.add(precursorCharge);
        
        } else {
        
            for (int charge = 1; charge < precursorCharge; charge++) {
            
                charges.add(charge);
            
            }
        }
        
        specificAnnotationPreferences.setSelectedCharges(charges);
        specificAnnotationPreferences.setSelectedIonsMap((HashMap<Ion.IonType, HashSet<Integer>>) selectedIonsMap.clone());
        specificAnnotationPreferences.setFragmentIonAccuracy(fragmentIonAccuracy);
        specificAnnotationPreferences.setFragmentIonPpm(fragmentIonPpm);
        return specificAnnotationPreferences;
        
    }

    /**
     * Constructor setting preferences from search parameters.
     *
     * @param searchParameters the search parameters
     */
    public AnnotationParameters(SearchParameters searchParameters) {
        setPreferencesFromSearchParameters(searchParameters);
    }

    /**
     * Updates the annotation parameters based on search parameters.
     *
     * @param searchParameters the search parameters where to take the
     * information from
     */
    public void setPreferencesFromSearchParameters(SearchParameters searchParameters) {
        clearIonTypes();
        for (Integer ion : searchParameters.getForwardIons()) {
            addIonType(Ion.IonType.PEPTIDE_FRAGMENT_ION, ion);
            addIonType(Ion.IonType.TAG_FRAGMENT_ION, ion);
        }
        for (Integer ion : searchParameters.getRewindIons()) {
            addIonType(Ion.IonType.PEPTIDE_FRAGMENT_ION, ion);
            addIonType(Ion.IonType.TAG_FRAGMENT_ION, ion);
        }
        addIonType(Ion.IonType.PRECURSOR_ION);
        addIonType(Ion.IonType.IMMONIUM_ION);
        addIonType(Ion.IonType.REPORTER_ION);
        addIonType(Ion.IonType.RELATED_ION);
        setFragmentIonAccuracy(searchParameters.getFragmentIonAccuracy());
        setFragmentIonPpm(searchParameters.getFragmentAccuracyType() == SearchParameters.MassAccuracyType.PPM);
        ModificationParameters ptmSettings = searchParameters.getModificationParameters();
        if (getReporterIons()) {
            HashSet<Integer> ptmReporterIons = IonFactory.getReporterIons(ptmSettings);
            selectedIonsMap.put(ReporterIon.IonType.REPORTER_ION, ptmReporterIons);
        }
        if (isAutomaticAnnotation() || areNeutralLossesSequenceAuto()) {
            ArrayList<NeutralLoss> neutralLosses = IonFactory.getNeutralLosses(searchParameters.getModificationParameters());
            for (NeutralLoss neutralLoss : neutralLosses) {
                addNeutralLoss(neutralLoss);
            }
        }
    }

    /**
     * Returns whether neutral losses are considered only for amino acids of
     * interest or not.
     *
     * @return a boolean indicating whether neutral losses are considered only
     * for amino acids of interest or not
     */
    public Boolean areNeutralLossesSequenceAuto() {
        return neutralLossesAuto;
    }

    /**
     * Sets whether neutral losses are considered only for amino acids of
     * interest or not.
     *
     * @param neutralLossesAuto a boolean indicating whether neutral losses are
     * considered only for amino acids of interest or not
     */
    public void setNeutralLossesSequenceAuto(Boolean neutralLossesAuto) {
        this.neutralLossesAuto = neutralLossesAuto;
    }

    /**
     * Indicates whether reporter ions should be annotated by default.
     *
     * @return a boolean indicating whether reporter ions should be annotated by
     * default
     */
    public Boolean getReporterIons() {
        if (reporterIons == null) {
            reporterIons = true;
        }
        return reporterIons;
    }

    /**
     * Sets whether reporter ions should be annotated by default.
     *
     * @param reporterIons a boolean indicating whether reporter ions should be
     * annotated by default
     */
    public void setReporterIons(Boolean reporterIons) {
        this.reporterIons = reporterIons;
    }

    /**
     * Indicates whether related ions should be annotated by default.
     *
     * @return a boolean indicating whether related ions should be annotated by
     * default
     */
    public Boolean getRelatedIons() {
        if (relatedIons == null) {
            relatedIons = true;
        }
        return relatedIons;
    }

    /**
     * Sets whether related ions should be annotated by default.
     *
     * @param relatedIons a boolean indicating whether related ions should be
     * annotated by default
     */
    public void setRelatedIons(Boolean relatedIons) {
        this.relatedIons = relatedIons;
    }

    /**
     * Clears the considered neutral losses.
     */
    public void clearNeutralLosses() {
        neutralLossesList.clear();
    }

    /**
     * Returns the considered neutral losses.
     *
     * @return the considered neutral losses
     */
    public ArrayList<NeutralLoss> getNeutralLosses() {
        if (neutralLossesList == null) {
            neutralLossesList = new ArrayList<>(2);
        }
        return neutralLossesList;
    }

    /**
     * Adds a neutral loss.
     *
     * @param neutralLoss a new neutral loss
     */
    public void addNeutralLoss(NeutralLoss neutralLoss) {
        if (neutralLossesList == null) {
            neutralLossesList = new ArrayList<>(2);
        }
        boolean alreadyInList = false;
        for (NeutralLoss tempNeutralLoss : neutralLossesList) {
            if (neutralLoss.isSameAs(tempNeutralLoss)) {
                alreadyInList = true;
                break;
            }
        }
        if (!alreadyInList) {
            neutralLossesList.add(neutralLoss);
        }
    }

    /**
     * Returns the type of ions annotated.
     *
     * @return the type of ions annotated
     */
    public HashMap<Ion.IonType, HashSet<Integer>> getIonTypes() {
        return selectedIonsMap;
    }

    /**
     * Returns the type of peptide fragment ions annotated.
     *
     * @return the type of peptide fragment ions annotated
     */
    public HashSet<Integer> getFragmentIonTypes() {
        if (selectedIonsMap.get(Ion.IonType.PEPTIDE_FRAGMENT_ION) == null) {
            return new HashSet<>();
        } else {
            return selectedIonsMap.get(Ion.IonType.PEPTIDE_FRAGMENT_ION); // @TOOO: what about tags..?
        }
    }

    /**
     * Adds a new ion type and subtype to annotate.
     *
     * @param ionType a new ion type to annotate
     * @param subType the ion sub type
     */
    public void addIonType(Ion.IonType ionType, int subType) {
        HashSet<Integer> selectedSubtypes = selectedIonsMap.get(ionType);
        if (selectedSubtypes == null) {
            selectedSubtypes = new HashSet<>(1);
            selectedIonsMap.put(ionType, selectedSubtypes);
        }
        selectedSubtypes.add(subType);

    }

    /**
     * Adds a new ion type to annotate. All subtypes will be annotated.
     *
     * @param ionType a new ion type to annotate
     */
    public void addIonType(Ion.IonType ionType) {

        int[] possibleSubtypes = Ion.getPossibleSubtypes(ionType);
        HashSet<Integer> selectedSubtypes = selectedIonsMap.get(ionType);

        if (selectedSubtypes == null) {

            selectedSubtypes = Arrays.stream(possibleSubtypes)
                    .mapToObj(Integer::new)
                    .collect(Collectors.toCollection(HashSet::new));
            selectedIonsMap.put(ionType, selectedSubtypes);

        } else {

            for (int i = 0; i < possibleSubtypes.length; i++) {

                selectedSubtypes.add(possibleSubtypes[i]);

            }
        }
    }

    /**
     * Clears the ion types annotated.
     */
    public void clearIonTypes() {
        selectedIonsMap.clear();
    }

    /**
     * Sets whether the annotation settings should be automatically inferred.
     *
     * @param automaticAnnotation a boolean indicating whether the annotation
     * settings should be automatically inferred
     */
    public void setAutomaticAnnotation(boolean automaticAnnotation) {
        this.automaticAnnotation = automaticAnnotation;

        if (automaticAnnotation) {
            neutralLossesAuto = true;
        }
    }

    /**
     * Returns whether the annotation settings should be automatically inferred.
     *
     * @return a boolean indicating whether the annotation settings should be
     * automatically inferred
     */
    public boolean isAutomaticAnnotation() {
        return automaticAnnotation;
    }

    /**
     * Returns the fragment ion accuracy.
     *
     * @return the fragment ion accuracy
     */
    public double getFragmentIonAccuracy() {
        return fragmentIonAccuracy;
    }

    /**
     * Returns the fragment ion accuracy in Da. If the tolerance is in ppm it
     * will be converted using the given reference mass.
     *
     * @param refMass the reference mass to use for the ppm to Da conversion
     *
     * @return the fragment ion accuracy
     */
    public double getFragmentIonAccuracyInDa(Double refMass) {
        if (fragmentIonPpm) {
            return fragmentIonAccuracy * refMass / 1000000;
        } else {
            return fragmentIonAccuracy;
        }
    }

    /**
     * Sets the fragment ion accuracy.
     *
     * @param fragmentIonAccuracy the fragment ion accuracy
     */
    public void setFragmentIonAccuracy(double fragmentIonAccuracy) {
        this.fragmentIonAccuracy = fragmentIonAccuracy;
    }

    /**
     * Indicates whether the fragment ion accuracy is in ppm.
     *
     * @return a boolean indicating whether the fragment ion accuracy is in ppm
     */
    public boolean isFragmentIonPpm() {
        if (fragmentIonPpm == null) { // Backward compatibility
            fragmentIonPpm = false;
        }
        return fragmentIonPpm;
    }

    /**
     * Sets whether the fragment ion accuracy is in ppm.
     *
     * @param fragmentIonPpm a boolean indicating whether the fragment ion
     * accuracy is in ppm
     */
    public void setFragmentIonPpm(boolean fragmentIonPpm) {
        this.fragmentIonPpm = fragmentIonPpm;
    }

    /**
     * Returns the intensity percentile to consider for annotation. e.g. 0.75
     * means that the 25% most intense peaks will be annotated.
     *
     * @return the intensityLimit
     */
    public double getAnnotationIntensityLimit() {
        return intensityLimit;
    }

    /**
     * Sets the intensity percentile to consider for annotation. e.g. 0.75 means
     * that the 25% most intense peaks will be annotated.
     *
     * @param intensityLimit the intensityLimit to set
     */
    public void setIntensityLimit(double intensityLimit) {
        this.intensityLimit = intensityLimit;
    }

    /**
     * Returns the intensity threshold type.
     *
     * @return the intensity threshold type
     */
    public IntensityThresholdType getIntensityThresholdType() {

        if (intensityThresholdType == null) { // Backward compatibility
            intensityThresholdType = IntensityThresholdType.percentile;
        }
        return intensityThresholdType;
    }

    /**
     * Sets the intensity threshold type.
     *
     * @param intensityThresholdType the intensity threshold type
     */
    public void setIntensityThresholdType(IntensityThresholdType intensityThresholdType) {
        this.intensityThresholdType = intensityThresholdType;
    }

    /**
     * If true, all peaks are shown, false displays the annotated peaks, and the
     * non-annotated in the background.
     *
     * @return true if all peaks are to be shown
     */
    public boolean showAllPeaks() {
        return showAllPeaks;
    }

    /**
     * Set if all peaks or just the annotated ones are to be shown.
     *
     * @param showAllPeaks if all peaks are to be shown
     */
    public void setShowAllPeaks(boolean showAllPeaks) {
        this.showAllPeaks = showAllPeaks;
    }

    /**
     * Returns true if the automatic y-axis zoom excludes background peaks.
     * False if includes all peaks.
     *
     * @return true if the automatic y-axis zoom excludes background peaks
     */
    public boolean yAxisZoomExcludesBackgroundPeaks() {
        return yAxisZoomExcludesBackgroundPeaks;
    }

    /**
     * Set if the automatic y-axis zoom only considers the annotated peaks.
     *
     * @param yAxisZoomExcludesBackgroundPeaks if the automatic y-axis zoom only
     * considers the annotated peaks
     */
    public void setYAxisZoomExcludesBackgroundPeaks(boolean yAxisZoomExcludesBackgroundPeaks) {
        this.yAxisZoomExcludesBackgroundPeaks = yAxisZoomExcludesBackgroundPeaks;
    }

    /**
     * Returns true if the automatic forward ion de novo tags are to be shown.
     *
     * @return the showForwardIonDeNovoTags
     */
    public boolean showForwardIonDeNovoTags() {
        return showForwardIonDeNovoTags;
    }

    /**
     * Set if the automatic forward ion de novo tags are to be shown.
     *
     * @param showForwardIonDeNovoTags the showForwardIonDeNovoTags to set
     */
    public void setShowForwardIonDeNovoTags(boolean showForwardIonDeNovoTags) {
        this.showForwardIonDeNovoTags = showForwardIonDeNovoTags;
    }

    /**
     * Returns true if the automatic rewind ion de novo tags are to be shown.
     *
     * @return the showRewindIonDeNovoTags
     */
    public boolean showRewindIonDeNovoTags() {
        return showRewindIonDeNovoTags;
    }

    /**
     * Set if the automatic rewind ion de novo tags are to be shown.
     *
     * @param showRewindIonDeNovoTags the showRewindIonDeNovoTags to set
     */
    public void setShowRewindIonDeNovoTags(boolean showRewindIonDeNovoTags) {
        this.showRewindIonDeNovoTags = showRewindIonDeNovoTags;
    }

    /**
     * Returns the charge to use for the fragment ions in the de novo
     * sequencing.
     *
     * @return the charge to use for the fragment ions in the de novo sequencing
     */
    public int getDeNovoCharge() {
        return deNovoCharge;
    }

    /**
     * Set the charge to use for the fragment ions in the de novo sequencing
     *
     * @param deNovoCharge the charge to use for the fragment ions in the de
     * novo sequencing
     */
    public void setDeNovoCharge(int deNovoCharge) {
        this.deNovoCharge = deNovoCharge;
    }

    /**
     * Returns true if the peak annotation should be based on the most accurate
     * mz value, false bases the annotation on the most intense peak.
     *
     * @deprecated use tiesResolution instead
     *
     * @return the highResolutionAnnotation
     */
    public boolean isHighResolutionAnnotation() {
        if (highResolutionAnnotation == null) {
            highResolutionAnnotation = true;
        }
        return highResolutionAnnotation;
    }

    /**
     * Set if the peak annotation should be based on the most accurate mz value,
     * or on the most intense peak.
     *
     * @deprecated use tiesResolution instead
     *
     * @param highResolutionAnnotation the highResolutionAnnotation to set
     */
    public void setHighResolutionAnnotation(boolean highResolutionAnnotation) {
        this.highResolutionAnnotation = highResolutionAnnotation;
    }

    /**
     * Returns the ties resolution method to use when multiple peaks can be
     * assigned to an ion.
     *
     * @return the ties resolution method to use
     */
    public SpectrumAnnotator.TiesResolution getTiesResolution() {
        if (tiesResolution == null) { // backward compatibility
            tiesResolution = highResolutionAnnotation ? SpectrumAnnotator.TiesResolution.mostAccurateMz : SpectrumAnnotator.TiesResolution.mostIntense;
        }
        return tiesResolution;
    }

    /**
     * Sets the ties resolution method to use when multiple peaks can be
     * assigned to an ion.
     *
     * @param tiesResolution the ties resolution method to use
     */
    public void setTiesResolution(SpectrumAnnotator.TiesResolution tiesResolution) {
        this.tiesResolution = tiesResolution;
    }

    /**
     * Clones the settings.
     *
     * @return a clone of this object
     */
    public AnnotationParameters clone() {
        AnnotationParameters annotationSettings = new AnnotationParameters();
        annotationSettings.setYAxisZoomExcludesBackgroundPeaks(yAxisZoomExcludesBackgroundPeaks);
        annotationSettings.setShowAllPeaks(showAllPeaks);
        annotationSettings.setIntensityLimit(intensityLimit);
        annotationSettings.setAutomaticAnnotation(automaticAnnotation);
        annotationSettings.setFragmentIonAccuracy(fragmentIonAccuracy);
        annotationSettings.setFragmentIonPpm(fragmentIonPpm);
        annotationSettings.setShowForwardIonDeNovoTags(showForwardIonDeNovoTags);
        annotationSettings.setShowRewindIonDeNovoTags(showRewindIonDeNovoTags);
        annotationSettings.setDeNovoCharge(deNovoCharge);
        annotationSettings.setTiesResolution(getTiesResolution());
        annotationSettings.setNeutralLossesSequenceAuto(neutralLossesAuto);
        annotationSettings.setReporterIons(getReporterIons());
        annotationSettings.setReporterIons(getRelatedIons());
        for (NeutralLoss neutralLoss : neutralLossesList) {
            annotationSettings.addNeutralLoss(neutralLoss);
        }
        for (Ion.IonType ionType : selectedIonsMap.keySet()) {
            for (Integer subType : selectedIonsMap.get(ionType)) {
                annotationSettings.addIonType(ionType, subType);
            }
        }
        return annotationSettings;
    }

    /**
     * Returns a boolean indicating whether the given annotation settings are
     * the same as these ones.
     *
     * @param annotationSettings the annotation settings to compare to
     *
     * @return a boolean indicating whether the given annotation settings are
     * the same as these ones
     */
    public boolean isSameAs(AnnotationParameters annotationSettings) {
        if (yAxisZoomExcludesBackgroundPeaks != annotationSettings.yAxisZoomExcludesBackgroundPeaks()) {
            return false;
        }
        if (showAllPeaks != annotationSettings.showAllPeaks()) {
            return false;
        }
        if (intensityThresholdType != annotationSettings.getIntensityThresholdType()) {
            return false;
        }
        if (intensityLimit != annotationSettings.getAnnotationIntensityLimit()) {
            return false;
        }
        if (automaticAnnotation != annotationSettings.isAutomaticAnnotation()) {
            return false;
        }
        if (fragmentIonAccuracy != annotationSettings.getFragmentIonAccuracy()) {
            return false;
        }
        if (fragmentIonPpm != annotationSettings.isFragmentIonPpm()) {
            return false;
        }
        if (showForwardIonDeNovoTags != annotationSettings.showForwardIonDeNovoTags()) {
            return false;
        }
        if (showRewindIonDeNovoTags != annotationSettings.showRewindIonDeNovoTags()) {
            return false;
        }
        if (deNovoCharge != annotationSettings.getDeNovoCharge()) {
            return false;
        }
        if (getTiesResolution() != annotationSettings.getTiesResolution()) {
            return false;
        }
        if (!neutralLossesAuto.equals(annotationSettings.areNeutralLossesSequenceAuto())) {
            return false;
        }
        if (!getReporterIons().equals(annotationSettings.getReporterIons())) {
            return false;
        }
        if (!getRelatedIons().equals(annotationSettings.getRelatedIons())) {
            return false;
        }
        ArrayList<NeutralLoss> otherNeutralLosses = annotationSettings.getNeutralLosses();
        if (getNeutralLosses().size() != otherNeutralLosses.size()) {
            return false;
        }
        for (NeutralLoss neutralLoss1 : getNeutralLosses()) {
            boolean found = false;
            for (NeutralLoss neutralLoss2 : otherNeutralLosses) {
                if (neutralLoss1.isSameAs(neutralLoss2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        HashMap<Ion.IonType, HashSet<Integer>> ionTypes = annotationSettings.getIonTypes();
        if (ionTypes.size() != selectedIonsMap.size()) {
            return false;
        }
        for (Ion.IonType ionType : ionTypes.keySet()) {
            HashSet<Integer> thisSubIons = selectedIonsMap.get(ionType);
            if (thisSubIons == null) {
                return false;
            }
            HashSet<Integer> otherSubIons = ionTypes.get(ionType);
            if (thisSubIons.size() != otherSubIons.size()) {
                return false;
            }
            for (Integer subIon1 : thisSubIons) {
                boolean found = false;
                for (Integer subIon2 : otherSubIons) {
                    if (subIon1.equals(subIon2)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a short description of the parameters.
     *
     * @return a short description of the parameters
     */
    public String getShortDescription() {

        String newLine = System.getProperty("line.separator");

        StringBuilder output = new StringBuilder();

        if (!selectedIonsMap.isEmpty()) {
            output.append("Ion Types: ");
            String ionTypes = "";

            for (Ion.IonType ionType : selectedIonsMap.keySet()) {
                if (null != ionType) {
                    switch (ionType) {
                        case IMMONIUM_ION:
                            if (!ionTypes.isEmpty()) {
                                ionTypes += ", ";
                            }
                            ionTypes += "immonium ions";
                            break;
                        case RELATED_ION:
                            if (!ionTypes.isEmpty()) {
                                ionTypes += ", ";
                            }
                            ionTypes += "related ions";
                            break;
                        case PEPTIDE_FRAGMENT_ION:
                            // @TODO: what about tags..?
                            for (int subType : selectedIonsMap.get(ionType)) {
                                switch (subType) {
                                    case PeptideFragmentIon.A_ION:
                                        if (!ionTypes.isEmpty()) {
                                            ionTypes += ", ";
                                        }
                                        ionTypes += "a ions";
                                        break;
                                    case PeptideFragmentIon.B_ION:
                                        if (!ionTypes.isEmpty()) {
                                            ionTypes += ", ";
                                        }
                                        ionTypes += "b ions";
                                        break;
                                    case PeptideFragmentIon.C_ION:
                                        if (!ionTypes.isEmpty()) {
                                            ionTypes += ", ";
                                        }
                                        ionTypes += "x ions";
                                        break;
                                    case PeptideFragmentIon.X_ION:
                                        if (!ionTypes.isEmpty()) {
                                            ionTypes += ", ";
                                        }
                                        ionTypes += "x ions";
                                        break;
                                    case PeptideFragmentIon.Y_ION:
                                        if (!ionTypes.isEmpty()) {
                                            ionTypes += ", ";
                                        }
                                        ionTypes += "y ions";
                                        break;
                                    case PeptideFragmentIon.Z_ION:
                                        if (!ionTypes.isEmpty()) {
                                            ionTypes += ", ";
                                        }
                                        ionTypes += "z ions";
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        case PRECURSOR_ION:
                            if (!ionTypes.isEmpty()) {
                                ionTypes += ", ";
                            }
                            ionTypes += "precursor ions";
                            break;
                        case REPORTER_ION:
                            if (!ionTypes.isEmpty()) {
                                ionTypes += ", ";
                            }
                            ionTypes += "reporter ions";
                            break;
                        default:
                            break;
                    }
                }
            }

            output.append(ionTypes).append(".").append(newLine);
        }

        ArrayList<NeutralLoss> selectedNeutralLosses = getNeutralLosses();

        if (!selectedNeutralLosses.isEmpty()) {
            output.append("Neutral Losses: ");
            String neutralLosses = "";

            for (NeutralLoss selectedNeutralLoss : selectedNeutralLosses) {
                if (!neutralLosses.isEmpty()) {
                    neutralLosses += ", ";
                }
                neutralLosses += selectedNeutralLoss.name;
            }

            output.append(neutralLosses).append(".").append(newLine);
        }

        output.append("Intensity Threshold Type: ").append(intensityThresholdType).append(".").append(newLine);
        output.append("Intensity Threshold: ").append(intensityLimit * 100).append(".").append(newLine);

        String unit;
        if (fragmentIonPpm) {
            unit = "ppm";
        } else {
            unit = "Da";
        }
        output.append("Fragment Ion Accuracy: ").append(fragmentIonAccuracy).append(" ").append(unit).append(".").append(newLine);

        output.append("Best peak selection: ").append(tiesResolution.description).append(".").append(newLine);

        return output.toString();
    }
}
