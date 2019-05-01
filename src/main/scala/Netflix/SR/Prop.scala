package Netflix.SR

import java.io.FileInputStream
import java.util.Properties

class Prop {

  def getProp(propertyName: String): String = {

    val properties = new Properties()
    properties.load(new FileInputStream("config.properties"))
    val property = properties.getProperty(propertyName)
    property.toString

  }



}
