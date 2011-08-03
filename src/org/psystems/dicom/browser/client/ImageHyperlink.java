package org.psystems.dicom.browser.client;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;

public class ImageHyperlink extends Hyperlink implements SourcesMouseEvents {

  private MouseListenerCollection mouseListeners;
private Image img;

  public ImageHyperlink(Image img) {
    this(img, "");
  }

  public ImageHyperlink(Image img, String targetHistoryToken) {
    super();
    
    this.img = img;
    DOM.appendChild(DOM.getFirstChild(getElement()), img.getElement());
    setTargetHistoryToken(targetHistoryToken);

    img.unsinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
  }
  
  public void setImage(Image imgNew) {
	  
	  DOM.removeChild(DOM.getFirstChild(getElement()), img.getElement());
	  this.img = imgNew;
	  DOM.appendChild(DOM.getFirstChild(getElement()), img.getElement());
	  img.unsinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
  }

  public void addMouseListener(MouseListener listener) {
    if (mouseListeners == null)
      mouseListeners = new MouseListenerCollection();
    mouseListeners.add(listener);
  }

  public void removeMouseListener(MouseListener listener) {
    if (mouseListeners != null)
      mouseListeners.remove(listener);
  }

  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    switch (DOM.eventGetType(event)) {
    case Event.ONMOUSEDOWN:
    case Event.ONMOUSEUP:
    case Event.ONMOUSEMOVE:
    case Event.ONMOUSEOVER:
    case Event.ONMOUSEOUT: {
      if (mouseListeners != null)
        mouseListeners.fireMouseEvent(this, event);
      break;
    }
    }
  }
}  