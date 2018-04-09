package a3a04group5.bookzilla;

import com.google.api.services.books.model.Volumes;

public interface OnTaskCompleted {
    void onTaskCompleted(Volumes volumes);
}