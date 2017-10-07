package test2

/**
  * Created by ee38732 on 12/09/17.
  */
object Main2 {
  def main(args: Array[String]): Unit = {

    println("Calvin")

    val person = Category("person")
    val employee = Category("employee", Set(person))
    val address = Category("address")

    val name = Text("name", person)
    val surname = Text("surname", person)
    val salary = Double("salary", employee)
    val city = Text("city", address)
    val street = Text("street", address)

    val ep =
      Structure(
        Property(name, "Enrico"),
        Property(surname, "Pigozzi"),
        Property(salary, 1234.567),
        Structure(
          Property(city, "Torino"),
          Property(street, "Via Asiago 14")
        )
      )

    println("ep: " + ep)

  }
}

abstract class Schema {
  val id: String

  override def toString = id
}

abstract class Type extends Schema {
  val category: Category

  override def toString = category + "." + id
}

case class Text(id: String, category: Category) extends Type

case class Double(id: String, category: Category) extends Type

case class Category(id: String, is: Set[Category] = Set()) extends Schema

abstract class Data

case class Property(schema: Schema, value: Any) extends Data {

  schema match {
    case dataType: Text => value.asInstanceOf[java.lang.String]
    case dataType: Double => value.asInstanceOf[java.lang.Double]
    case dataType: Category => value.asInstanceOf[Category]
  }

  override def toString = schema + ":" + value
}

object Structure {
  def apply(data: Data*) = new Structure(data.toSet)
}

class Structure(val data: Set[Data]) extends Data {

  def properties = flatProperties(data)

  override def toString = data.mkString(", ")

  private def flatProperties(data: Set[Data]): Set[Property] =
    data.flatMap {
      case property: Property => Set[Property](property)
      case structure: Structure => flatProperties(structure.data)
    }
}