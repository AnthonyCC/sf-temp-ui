package com.freshdirect.cms.ui.client.action;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.freshdirect.cms.ui.client.NewKeySet;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.publish.GwtValidationError;
import com.google.gwt.user.client.History;

public class SaveNodeAction extends BasicAction<GwtSaveResponse> {

    public SaveNodeAction() {
        final GwtNodeData nodeData = ManageStoreView.getInstance().getCurrentNode();

        startSaveProgress("Saving changes.", "Saving ...");

        if (nodeData != null) {
            // add this node
            WorkingSet.add(nodeData.getNode());

            // add any nodes that were changed because of VariationMatrix or CustomGrid components
            nodeData.collectValuesFromFields();

            // TODO check this functionality : collectValuesFromFields does change the currentnode, and has some side effects ...
            // important thing is : adds changed nodes to the workingset!
        }
    }

    @Override
    public void onSuccess(GwtSaveResponse result) {
        final ManageStoreView ml = ManageStoreView.getInstance();

        if (result.isOk()) {
            WorkingSet.clear();
            NewKeySet.clear();
            History.newItem(null);

            // display changeset of save operation
            ml.setupChangesetLayout(result.getChangeSet());

            // nodeTree.getSelectionModel().deselectAll();
            stopProgress();
            ContentTreePopUp.setForceReload(true);

            ml.getMainTree().invalidate();
        } else {
            StringBuilder errorMessage = new StringBuilder("Error:");

            if (result.getErrorMessage() != null) {
                errorMessage.append(result.getErrorMessage());
            }

            if (result.getValidationMessages() != null) {
                for (GwtValidationError g : result.getValidationMessages()) {
                    errorMessage.append(g.getHumanReadable());
                }
            }

            stopProgress();

            MessageBox.alert("Errors", errorMessage.toString(), null);
        }
    }

}
