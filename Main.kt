// main

//constant for printing the menu
val instructions : String = "\n\n\n\n" +
        "-------------------------------------------------------\n" +
        "BNF Grammar | Example : start R <= S * T ; U <= R stop \n" +
        "-------------------------------------------------------\n" +
        "   <program>->  start <stmt_list> stop\n" +
        " <stmt_list>->  <stmt>\n" +
        "             |  <stmt> ; <stmt_list>\n" +
        "      <stmt>->  <var> = <expr>\n" +
        "       <var>->  R | S | T | U\n" +
        "      <expr>->  <var> / <var>\n" +
        "             |  <var> * <var>\n" +
        "             |  <var>\n" +
        "-------------------------------------------------------\n" +
        " Enter \"HALT\" to terminate program\n" +
        "-------------------------------------------------------\n"
fun main() {

    // main program loop for the bnf display and input string
    promptLoop()

}

fun promptLoop(){
    while (true) {
        print(instructions)

        // prompting the user for the input string
        // read in the input from the user
        print("input string: ")
        var consoleInput  = readLine()

        //checking if input is null
        if (consoleInput != null){

            //checking for terminating word
            if (consoleInput == "HALT"){
                println("Thank you for compiling")
                break
            }
            else
            {
                //validating the console input
                var program = languageRecognizer(consoleInput.orEmpty())

                if(!program.hasError){
                    var parse_tree = parseTree(program.getFormattedList())
                    parse_tree.printTree()
                }
            }
        }
    }
}