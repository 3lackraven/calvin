package test1

/**
  * Created by ee38732 on 12/09/17.
  */
object Main {
  def main(args: Array[String]): Unit = {

    println("Calvin")

    val name = Text("name")
    val surname = Text("surname")
    val salary = Text("salary")

    val enrico = Property(name, "Enrico")
    val pigozzi = Property(surname, "Pigozzi")
    val epSalary = Property(salary, 1234.567)

    val person = Category("person", Set(), Set(name, surname))
    val employee = Category("employee", Set(person), Set(salary))
    //val person = Category("person", Set(), Set(name, surname))

    val ep = Structure(Set(employee), Set(enrico, pigozzi, epSalary))

    println("ep: " + ep)

  }
}

abstract class Meta {

  val id: String

  override def toString = id
}

case class Text(id: String) extends Meta

case class Double(id: String) extends Meta

case class Category(id: String, is: Set[Category], has: Set[Meta]) extends Meta

abstract class Data

case class Property(meta: Meta, value: Any) extends Data {
  override def toString = meta + ":" + value
}

case class Structure(is: Set[Category], has: Set[Data]) extends Data {
  override def toString = {

    //println("is: " + is)
    //println("has: " + has)

    def expandCategory(categories: Set[Category]): Set[Category] =
      categories.flatMap(category => expandCategory(category.is) + category)

    expandCategory(is)
      .map(category => {

        //println("category: " + category)
        //println("category.is: " + category.is)
        //println("category.has: " + category.has)

        val data: Set[Data] = has.filter {
          case property: Property =>
            category.has.contains(property.meta)

          case structure: Structure =>
            structure.is.contains(category)
        }

        //println("found data: " + data)

        category + "(" + data.mkString(", ") + ")"
      }).mkString(", ")
  }

}

