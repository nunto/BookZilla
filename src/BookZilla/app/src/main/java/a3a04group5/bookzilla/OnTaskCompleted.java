package a3a04group5.bookzilla;

import com.google.api.services.books.model.Volumes;
// Interface that allows retrieval of value from an AsynTask
public interface OnTaskCompleted {
    void onTaskCompleted(Volumes volumes);
}