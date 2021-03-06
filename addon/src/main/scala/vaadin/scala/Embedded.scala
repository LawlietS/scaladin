package vaadin.scala

import vaadin.scala.mixins.EmbeddedMixin
import scala.collection.mutable

package mixins {
  trait EmbeddedMixin extends AbstractComponentMixin
}

object Embedded {
  object Type extends Enumeration {
    import com.vaadin.ui.Embedded._
    val objectType = Value(TYPE_OBJECT)
    val image = Value(TYPE_IMAGE)
    val browser = Value(TYPE_BROWSER)
  }
}

/**
 * @see com.vaadin.ui.Embedded
 * @author Henri Kerola / Vaadin
 */
class Embedded(override val p: com.vaadin.ui.Embedded with EmbeddedMixin = new com.vaadin.ui.Embedded with EmbeddedMixin) extends AbstractComponent(p) {

  lazy val parameters: mutable.Map[String, String] = new mutable.Map[String, String] {
    def -=(name: String): this.type = { p.removeParameter(name); this }
    def +=(parameter: (String, String)): this.type = { update(parameter._1, parameter._2); this }
    override def update(name: String, value: String) {
      p.setParameter(name, value)
      this
    }
    def get(name: String) = Option(p.getParameter(name))
    override def size = {
      import scala.collection.JavaConversions._
      p.getParameterNames.size
    }
    def iterator: Iterator[(String, String)] = {
      import scala.collection.JavaConversions._
      p.getParameterNames.map { name =>
        (name, p.getParameter(name))
      }
    }
  }

  // TODO: the same clickListeners can be found from Panel, use a trait instead of copy-pasting? 
  lazy val clickListeners = new ListenersTrait[ClickEvent, ClickListener] {
    override def listeners = p.getListeners(classOf[com.vaadin.event.MouseEvents.ClickListener])
    override def addListener(elem: ClickEvent => Unit) = p.addListener(new ClickListener(elem))
    override def removeListener(elem: ClickListener) = p.removeListener(elem)
  }

  def alternateText: Option[String] = Option(p.getAlternateText)
  def alternateText_=(alternateText: String) = p.setAlternateText(alternateText)
  def alternateText_=(alternateText: Option[String]) = p.setAlternateText(alternateText.getOrElse(null))

  // TODO: better name than objectType?
  def objectType: Embedded.Type.Value = Embedded.Type(p.getType)
  def objectType_=(objectType: Embedded.Type.Value) = p.setType(objectType.id)

  def source: Option[Resource] = wrapperFor[Resource](p.getSource)
  def source_=(source: Option[Resource]) = if (source.isDefined) p.setSource(source.get.p) else p.setSource(null)
  def source_=(source: Resource) = if (source == null) p.setSource(null) else p.setSource(source.p)

  def codebase: Option[String] = Option(p.getCodebase);
  def codebase_=(codebase: Option[String]) = p.setCodebase(codebase.getOrElse(null))
  def codebase_=(codebase: String) = p.setCodebase(codebase)

  def codetype: Option[String] = Option(p.getCodetype);
  def codetype_=(codetype: Option[String]) = p.setCodetype(codetype.getOrElse(null))
  def codetype_=(codetype: String) = p.setCodetype(codetype)

  def standby: Option[String] = Option(p.getStandby);
  def standby_=(standby: Option[String]) = p.setStandby(standby.getOrElse(null))
  def standby_=(standby: String) = p.setStandby(standby)

  def mimeType: Option[String] = Option(p.getMimeType);
  def mimeType_=(mimeType: Option[String]) = p.setMimeType(mimeType.getOrElse(null))
  def mimeType_=(mimeType: String) = p.setMimeType(mimeType)

  def classId: Option[String] = Option(p.getClassId);
  def classId_=(classId: Option[String]) = p.setClassId(classId.getOrElse(null))
  def classId_=(classId: String) = p.setClassId(classId)

  def archive: Option[String] = Option(p.getArchive);
  def archive_=(archive: Option[String]) = p.setArchive(archive.getOrElse(null))
  def archive_=(archive: String) = p.setArchive(archive)
}