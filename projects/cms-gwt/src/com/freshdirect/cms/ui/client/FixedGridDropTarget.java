package com.freshdirect.cms.ui.client;

import java.util.List;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.dnd.Insert;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.Element;

public class FixedGridDropTarget extends GridDropTarget {

	public FixedGridDropTarget(Grid<? extends ModelData> grid) {
		super(grid);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onDragDrop(DNDEvent e) {		
	    Object data = e.getData();
	   
	    if (feedback == Feedback.APPEND || insertIndex >= grid.getStore().getCount()) {
	    	if (data instanceof ModelData) {
	    		grid.getStore().add((ModelData) data);
	    	} else if (data instanceof List) {
	    		grid.getStore().add((List<ModelData>) data);
	    	}
	    } else {
	    	if (data instanceof ModelData) {
	    		grid.getStore().insert((ModelData) data, insertIndex);
	    	} else if (data instanceof List) {
	    		grid.getStore().insert((List<ModelData>) data, insertIndex);
	    	}
	    }
	}
	
	@Override
	  protected void showFeedback(DNDEvent event) {
	    if (feedback == Feedback.INSERT) {
	      event.getStatus().setStatus(true);
	      Element row = grid.getView().findRow(event.getTarget()).cast();

	      if (row == null && grid.getStore().getCount() > 0) {
	        row = grid.getView().getRow(grid.getStore().getCount() - 1).cast();
	      }

	      if (row != null) {
	        int height = row.getOffsetHeight();
	        int mid = height / 2;
	        mid += row.getAbsoluteTop();
	        int y = event.getClientY();
	        boolean before = y < mid;
	        int idx = grid.getView().findRowIndex(row);
	        if (before && idx > 0) {
	            // 'before' means that the first half of the row, so if the pointer is in the first half of the row, then we want to insert before this row.
	            idx --;
	        }
	        insertIndex = idx;
	        
	        if (before) {
	          showInsert(event, row, true);
	        } else {
	          showInsert(event, row, false);
	        }
	      }
	    }
	}

    private void showInsert(DNDEvent event, Element row, boolean before) {
        Insert insert = Insert.get();
        insert.setVisible(true);
        Rectangle rect = El.fly(row).getBounds();
        int y = !before ? (rect.y + rect.height - 4) : rect.y - 2;
        insert.el().setBounds(rect.x, y, rect.width, 6);
      }
}
