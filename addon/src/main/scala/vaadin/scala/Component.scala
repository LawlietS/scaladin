package vaadin.scala

import scala.collection.mutable
import vaadin.scala.mixins.ComponentMixin
import vaadin.scala.mixins.AbstractComponentMixin
import vaadin.scala.mixins.ScaladinMixin

package mixins {

  trait ScaladinMixin {
    private[this] var _wrapper: Wrapper = _
    def wrapper = _wrapper
    def wrapper_=(wrapper: Wrapper) = {
      if (_wrapper != null)
        throw new RuntimeException("wrapper cannot be reset once set")
      else
        _wrapper = wrapper
    }
  }

  trait ComponentMixin extends ScaladinMixin
  trait AbstractComponentMixin extends ComponentMixin
}

trait Component extends Wrapper {
  def p: com.vaadin.ui.Component with ComponentMixin

  // TODO: add methods styleName, addStyleName, removeStyleName?

  def styleName = p.getStyleName
  def styleName_=(styleName: String) = p.setStyleName(styleName)

  val styleNames = new mutable.Set[String] {
    def contains(key: String) = p.getStyleName().split(" ").iterator.contains(key)
    def iterator: Iterator[String] = p.getStyleName().split(" ").iterator
    def +=(elem: String) = { elem.split(" ").foreach(p.addStyleName(_)); this }
    def -=(elem: String) = { p.removeStyleName(elem); this }
  }

  def enabled = p.isEnabled
  def enabled_=(enabled: Boolean) = p.setEnabled(enabled)

  def visible = p.isVisible
  def visible_=(visible: Boolean) = p.setVisible(visible)

  // TODO parent setter?
  def parent: Option[Component] = wrapperFor[Component](p.getParent())

  //FIXME: also defined in property, figure out what to do
  //  def readOnly = p.isReadOnly
  //  def readOnly_=(readOnly: Boolean) = p.setReadOnly(readOnly)

  def caption = Option(p.getCaption)
  def caption_=(caption: Option[String]) = p.setCaption(caption.getOrElse(null))
  def caption_=(caption: String) = p.setCaption(caption)

  def icon: Option[Resource] = wrapperFor[Resource](p.getIcon)
  def icon_=(icon: Option[Resource]) = if (icon.isDefined) p.setIcon(icon.get.p) else p.setIcon(null)
  def icon_=(icon: Resource) = if (icon == null) p.setIcon(null) else p.setIcon(icon.p)

  def window = wrapperFor[Window](p.getWindow)

  def application = Option(p.getApplication)

  def locale = Option(p.getLocale)

  def id = Option(p.getDebugId())
  def id_=(id: Option[String]) = p.setDebugId(id.getOrElse(null))
  def id_=(id: String) = p.setDebugId(id)

  //override def toString = p.toString

  // TODO: ..
}

trait ScaladinWrapper extends com.vaadin.ui.Component with Component with ComponentMixin {
  def p: this.type = this
  p.wrapper = this
}

trait Sizeable extends Component {

  def width: Option[Measure] = if (p.getWidth < 0) None else Option(Measure(p.getWidth, Units(p.getWidthUnits)))
  def width_=(width: Option[Measure]) = p.setWidth(if (width.isDefined) width.get.toString else null)
  def width_=(width: Measure) = p.setWidth(if (width != null) width.toString else null)

  def height: Option[Measure] = if (p.getHeight() < 0) None else Option(Measure(p.getHeight(), Units(p.getHeightUnits)))
  def height_=(height: Option[Measure]) = p.setHeight(if (height.isDefined) height.get.toString else null)
  def height_=(height: Measure) = p.setHeight(if (height != null) height.toString else null)

  def sizeFull() = p.setSizeFull
  def sizeUndefined() = p.setSizeUndefined
}

abstract class AbstractComponent(val p: com.vaadin.ui.AbstractComponent with AbstractComponentMixin) extends Component with Sizeable {

  p.wrapper = this

  def description = Option(p.getDescription)
  def description_=(description: Option[String]) = p.setDescription(description.getOrElse(null))
  def description_=(description: String) = p.setDescription(description)

  def immediate = p.isImmediate();
  def immediate_=(immediate: Boolean) = p.setImmediate(immediate);

  def data_=(data: Any) = p.setData(data)
  def data = p.getData
  
  def requestRepaint() = p.requestRepaint()
}

trait Focusable extends Component {

  def p: com.vaadin.ui.Component.Focusable with ComponentMixin

  def focus() = p.focus()

  def tabIndex = p.getTabIndex()
  def tabIndex_=(tabIndex: Int) = p.setTabIndex(tabIndex)
}

