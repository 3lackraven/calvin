package test3

/**
  * Created by ee38732 on 12/09/17.
  */
object Main3 {
  def main(args: Array[String]): Unit = {

    println("Calvin")

    val person = Category("person")
    val employee = Category("employee", Set(person))
    val address = Category("address")

    val name = new Type("name", person)
    val surname = new Type("surname", person)
    val salary = new Type("salary", employee)
    val city = new Type("city", address)
    val street = new Type("street", address)

    val ep =
      Structure(
        Set(
          Property(name, "Enrico"),
          Property(surname, "Pigozzi"),
          Property(salary, 1234.567),
          Structure(
            Set(
              Property(city, "Torino"),
              Property(street, "Via Asiago 14")
            )

          )
        )
      )

    println("ep: " + ep)

  }
}

abstract class Schema {
  val id: String

  override def toString = id
}

case class Type(id:String, category:Category) extends Schema {

  override def toString = category + "." + id
}

//case class Text(id: String, category: Category) extends Type

//case class Double(id: String, category: Category) extends Type

case class Category(id: String, is: Set[Category] = Set()) extends Schema

abstract class Data

case class Property(schema: Schema, value: Any) extends Data {

//  schema match {
//
//    case t:Type[_] =>
//
//     val x =  t.ct.tpe match {
//        case TypeRef(utype, usymbol, args) =>
//          List(utype, usymbol, args).mkString("/")
//      }
//
//      println("classType: " + x)
//      println("valueClass: " + value.getClass)
//      //println("classType: " + value.getClass.isAssignableFrom(t.ct))
//
//    case category:Category => println("category: " + category)
//  }

  override def toString = schema + ":" + value
}

case class Structure(data: Set[Data]) extends Data {

  def properties = flatProperties(data)

  override def toString = data.mkString(", ")

  private def flatProperties(data: Set[Data]): Set[Property] =
    data.flatMap {
      case property: Property => Set[Property](property)
      case structure: Structure => flatProperties(structure.data)
    }
}