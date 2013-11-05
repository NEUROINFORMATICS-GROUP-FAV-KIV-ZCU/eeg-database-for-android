/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.*;

import java.util.Collection;

/**
 * Helper class for filling detail lists.
 *
 * @author Petr Miko
 */
public final class ExperimentDetailLists {

    /**
     * Fills viewGroup with rows of electrode locations data.
     *
     * @param viewGroup          viewGroup to fill, usually layout
     * @param electrodeLocations collection of electrode locations
     */
    public static void fillElectrodeLocations(final ViewGroup viewGroup, Collection<ElectrodeLocation> electrodeLocations) {
        if (electrodeLocations != null && !electrodeLocations.isEmpty()) {

            //create and inflate electrode locations row by row
            final LayoutInflater inflater = getLayoutInflater(viewGroup);

            for (final ElectrodeLocation record : electrodeLocations) {
                View row = inflater.inflate(R.layout.base_electrode_location_row, viewGroup, false);

                TextView electrodeId = (TextView) row.findViewById(R.id.row_electrode_location_id);
                TextView electrodeAbbr = (TextView) row.findViewById(R.id.row_electrode_location_abbr);
                TextView electrodeTitle = (TextView) row.findViewById(R.id.row_electrode_location_title);
                TextView electrodeDescription = (TextView) row.findViewById(R.id.row_electrode_location_description);

                if (electrodeId != null) {
                    electrodeId.setText(Integer.toString(record.getId()));
                }

                if (electrodeAbbr != null) {
                    electrodeAbbr.setText(record.getAbbr());
                }

                if (electrodeTitle != null) {
                    electrodeTitle.setText(record.getTitle());
                }

                if (electrodeDescription != null) {
                    electrodeDescription.setText(record.getDescription());
                }

                row.setBackgroundResource(R.drawable.list_selector);
                viewGroup.addView(row);

                //every row has its detail dialog
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getElectrodeLocationDetailDialog(inflater, viewGroup, record).show();
                    }
                });
            }
        } else {
            setNoRecord(viewGroup);
        }
    }

    /**
     * Fills viewGroup with rows of pharmaceutical data
     *
     * @param viewGroup       viewGroup to fill, usually layout
     * @param pharmaceuticals collection of pharmaceutical data
     */
    public static void fillPharmaceuticals(final ViewGroup viewGroup, Collection<Pharmaceutical> pharmaceuticals) {
        if (pharmaceuticals != null && !pharmaceuticals.isEmpty()) {
            //create and inflate row by row
            final LayoutInflater inflater = getLayoutInflater(viewGroup);
            for (final Pharmaceutical record : pharmaceuticals) {
                View row = inflater.inflate(R.layout.base_pharmaceutical_row, viewGroup, false);

                TextView pharmId = (TextView) row.findViewById(R.id.row_software_id);
                TextView pharmTitle = (TextView) row.findViewById(R.id.row_software_title);
                TextView pharmDescription = (TextView) row.findViewById(R.id.row_software_description);

                if (pharmId != null) {
                    pharmId.setText(Integer.toString(record.getId()));
                }
                if (pharmTitle != null) {
                    pharmTitle.setText(record.getTitle());
                }
                if (pharmDescription != null) {
                    pharmDescription.setText(record.getDescription());
                }

                //every row has its detail dialog
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPharmaceuticDetailDialog(inflater, viewGroup, record).show();
                    }
                });

                row.setBackgroundResource(R.drawable.list_selector);
                viewGroup.addView(row);
            }
        } else {
            setNoRecord(viewGroup);
        }
    }

    /**
     * Fills viewGroup with rows of software data
     *
     * @param viewGroup    viewGroup to fill, usually layout
     * @param softwareList collection of software data
     */
    public static void fillSoftwareList(final ViewGroup viewGroup, Collection<Software> softwareList) {
        if (softwareList != null && !softwareList.isEmpty()) {

            //create and inflate row by row
            final LayoutInflater inflater = getLayoutInflater(viewGroup);
            for (final Software record : softwareList) {
                View row = inflater.inflate(R.layout.base_software_row, viewGroup, false);

                TextView swId = (TextView) row.findViewById(R.id.row_software_id);
                TextView swTitle = (TextView) row.findViewById(R.id.row_software_title);
                TextView swDescription = (TextView) row.findViewById(R.id.row_software_description);

                if (swId != null) {
                    swId.setText(Integer.toString(record.getId()));
                }
                if (swTitle != null) {
                    swTitle.setText(record.getTitle());
                }
                if (swDescription != null) {
                    swDescription.setText(record.getDescription());
                }

                //every row has its detail dialog
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSoftwareDetailDialog(inflater, viewGroup, record).show();
                    }
                });

                row.setBackgroundResource(R.drawable.list_selector);
                viewGroup.addView(row);
            }
        } else {
            setNoRecord(viewGroup);
        }
    }

    /**
     * Fills viewGroup with rows of hardware data.
     *
     * @param viewGroup    viewGroup to fill, usually layout
     * @param hardwareList collection of hardware data
     */
    public static void fillHardwareList(final ViewGroup viewGroup, Collection<Hardware> hardwareList) {
        if (hardwareList != null && !hardwareList.isEmpty()) {

            //create and inflate row by row
            final LayoutInflater inflater = getLayoutInflater(viewGroup);

            for (final Hardware record : hardwareList) {
                View row = inflater.inflate(R.layout.base_hardware_row, viewGroup, false);

                TextView hwId = (TextView) row.findViewById(R.id.row_hardware_id);
                TextView hwTitle = (TextView) row.findViewById(R.id.row_hardware_title);
                TextView hwType = (TextView) row.findViewById(R.id.row_hardware_type);

                if (hwId != null) {
                    hwId.setText(Integer.toString(record.getHardwareId()));
                }
                if (hwTitle != null) {
                    hwTitle.setText(record.getTitle());
                }
                if (hwType != null) {
                    hwType.setText(record.getType());
                }

                //every row has its detail dialog
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getHardwareDetailDialog(inflater, viewGroup, record).show();
                    }
                });

                row.setBackgroundResource(R.drawable.list_selector);
                viewGroup.addView(row);
            }
        } else {
            setNoRecord(viewGroup);
        }
    }

    /**
     * Fills viewGroup with rows of disease information.
     *
     * @param viewGroup viewGroup to fill, usually layout
     * @param diseases  collection with disease information
     */
    public static void fillDiseaseList(final ViewGroup viewGroup, Collection<Disease> diseases) {
        if (diseases != null && !diseases.isEmpty()) {

            //create and inflate row by row
            final LayoutInflater inflater = getLayoutInflater(viewGroup);
            for (final Disease record : diseases) {
                View row = inflater.inflate(R.layout.base_disease_row, viewGroup, false);

                TextView diseaseName = (TextView) row.findViewById(R.id.row_disease_name);
                TextView diseaseDescription = (TextView) row.findViewById(R.id.row_disease_description);

                if (diseaseName != null) {
                    diseaseName.setText(record.getName());
                }
                if (diseaseDescription != null) {
                    diseaseDescription.setText(record.getDescription());
                }

                //every row has its detail dialog
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDiseaseDetailDialog(inflater, viewGroup, record).show();
                    }
                });

                row.setBackgroundResource(R.drawable.list_selector);

                viewGroup.addView(row);
            }

        } else {
            setNoRecord(viewGroup);
        }
    }

    /**
     * Creates and sets builder of electrode location detail dialog.
     *
     * @param inflater  layout inflater
     * @param viewGroup viewGroup to fill, usually layout
     * @param record    record with data for filling the viewGroup
     * @return builder with dialog ready to show
     */
    private static AlertDialog.Builder getElectrodeLocationDetailDialog(final LayoutInflater inflater, ViewGroup viewGroup, ElectrodeLocation record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
        builder.setTitle(R.string.dialog_electrode_location_detail);
        View view = inflater.inflate(R.layout.base_electrode_dialog_details, null);

        TextView id = (TextView) view.findViewById(R.id.dialog_id);
        TextView abbr = (TextView) view.findViewById(R.id.dialog_abbr);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        TextView description = (TextView) view.findViewById(R.id.dialog_description);

        TextView typeTitle = (TextView) view.findViewById(R.id.dialog_type_title);
        TextView typeDescription = (TextView) view.findViewById(R.id.dialog_type_description);
        TextView fixTitle = (TextView) view.findViewById(R.id.dialog_fix_title);
        TextView fixDescription = (TextView) view.findViewById(R.id.dialog_fix_description);

        id.setText(Integer.toString(record.getId()));
        title.setText(record.getTitle());
        abbr.setText(record.getAbbr());
        description.setText(record.getDescription());
        typeTitle.setText(record.getElectrodeType().getTitle());
        typeDescription.setText(record.getElectrodeType().getDescription());
        fixTitle.setText(record.getElectrodeFix().getTitle());
        fixDescription.setText(record.getElectrodeFix().getDescription());

        return builder.setView(view);
    }

    /**
     * Creates and sets builder of electrode location detail dialog.
     *
     * @param inflater  layout inflater
     * @param viewGroup viewGroup to fill, usually layout
     * @param record    record with data for filling the viewGroup
     * @return builder with dialog ready to show
     */
    private static AlertDialog.Builder getPharmaceuticDetailDialog(final LayoutInflater inflater, ViewGroup viewGroup, Pharmaceutical record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());

        builder.setTitle(R.string.dialog_pharmaceutical_detail);
        View view = inflater.inflate(R.layout.base_pharmaceutical_dialog_details, null);

        TextView id = (TextView) view.findViewById(R.id.dialog_id);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        TextView description = (TextView) view.findViewById(R.id.dialog_description);

        id.setText(Integer.toString(record.getId()));
        title.setText(record.getTitle());
        description.setText(record.getDescription());

        return builder.setView(view);
    }

    /**
     * Creates and sets builder of software detail dialog.
     *
     * @param inflater  layout inflater
     * @param viewGroup viewGroup to fill, usually layout
     * @param record    record with data for filling the viewGroup
     * @return builder with dialog ready to show
     */
    private static AlertDialog.Builder getSoftwareDetailDialog(final LayoutInflater inflater, ViewGroup viewGroup, Software record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());

        builder.setTitle(R.string.dialog_software_detail);
        View view = inflater.inflate(R.layout.base_software_dialog_details, null);

        TextView id = (TextView) view.findViewById(R.id.dialog_id);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        TextView description = (TextView) view.findViewById(R.id.dialog_description);

        id.setText(Integer.toString(record.getId()));
        title.setText(record.getTitle());
        description.setText(record.getDescription());

        return builder.setView(view);
    }

    /**
     * Creates and sets builder of hardware detail dialog.
     *
     * @param inflater  layout inflater
     * @param viewGroup viewGroup to fill, usually layout
     * @param record    record with data for filling the viewGroup
     * @return builder with dialog ready to show
     */
    private static AlertDialog.Builder getHardwareDetailDialog(LayoutInflater inflater, ViewGroup viewGroup, Hardware record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
        builder.setTitle(R.string.dialog_hardware_detail);
        View view = inflater.inflate(R.layout.base_hardware_dialog_details, null);

        TextView id = (TextView) view.findViewById(R.id.dialog_id);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        TextView description = (TextView) view.findViewById(R.id.dialog_description);
        TextView type = (TextView) view.findViewById(R.id.dialog_type);

        id.setText(Integer.toString(record.getHardwareId()));
        title.setText(record.getTitle());
        description.setText(record.getDescription());
        type.setText(record.getType());

        return builder.setView(view);
    }

    /**
     * Creates and sets builder of disease detail dialog.
     *
     * @param inflater  layout inflater
     * @param viewGroup viewGroup to fill, usually layout
     * @param record    record with data for filling the viewGroup
     * @return builder with dialog ready to show
     */
    private static AlertDialog.Builder getDiseaseDetailDialog(LayoutInflater inflater, View viewGroup, Disease record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
        builder.setTitle(R.string.dialog_disease_detail);
        View view = inflater.inflate(R.layout.base_disease_dialog_details, null);

        TextView id = (TextView) view.findViewById(R.id.dialog_id);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        TextView description = (TextView) view.findViewById(R.id.dialog_description);

        id.setText(Integer.toString(record.getDiseaseId()));
        title.setText(record.getName());
        description.setText(record.getDescription());

        return builder.setView(view);
    }

    /**
     * Sets into viewGroup, that no record was selected.
     *
     * @param viewGroup parent viewGroup, usually layout
     */
    private static void setNoRecord(ViewGroup viewGroup) {
        //inflate information, that no record is available
        TextView row = new TextView(viewGroup.getContext());
        row.setText(R.string.dummy_none);
        viewGroup.addView(row);
    }

    /**
     * Returns layout inflater.
     *
     * @param viewGroup parent view group (usually layout)
     * @return layout inflater
     */
    private static LayoutInflater getLayoutInflater(ViewGroup viewGroup) {
        return (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
