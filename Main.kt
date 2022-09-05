// main
fun main() {

    // main program loop for the bnf display and input string
    while (true) {
        print(
            "\n-------------------------------------\nBNF Grammer\n-------------------------------------\n   <program>->  begin <stmt_list> end\n <stmt_list>->  <stmt>\n             |  <stmt> ; <stmt_list>\n      <stmt>->  <var> = <expr>\n       <var>->  A | B | C\n      <expr>->  <var> + <var>\n             |  <var> - <var>\n             |  <var>\n-------------------------------------\n"
        )

        // prompting the user for the input string
        // reaind the input from the user
        print("input string: ")
        var consoleInput  = readLine()

        //checking if input is null
        if (consoleInput != null){

            //checking for terminating word
            if (consoleInput == "HALT"){
                println("Thank you for compling")
                break
            }
            else
            {
                //validating the console input
                var program = languageRecognizer(consoleInput.orEmpty())

                if(program.hasError){
                    println("...")
                    println(program.hasError)
                }
                else {
                    println("Code is correct michael")
                    println(program.hasError)
                }
            }
        }
    }
}