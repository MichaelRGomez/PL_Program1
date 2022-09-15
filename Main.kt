// main
fun main() {

    // main program loop for the bnf display and input string
    while (true) {
        print(
        "\n\n\n\n" +
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
        )

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
                    println("Code is correct")
                }
            }
        }
    }
}