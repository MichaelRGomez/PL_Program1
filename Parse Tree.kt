//actual class
class parseTree  constructor(formatted_string : List<String>){
    //editable copy of the input_string in list form
    private var sen : MutableList<String> = formatted_string.toMutableList()

    //list that'll hold all the string that'll display the parse tree
    private var printingList : MutableList<String> = emptyList<String>().toMutableList()

    //prefix for format the string inside the printinglist
    private var pre : prefix = prefix()

    private var semiColons : Int = countSemiColons(sen)

    //function that'll print the parse tree
    public fun printTree(){
        println("\n Parse Tree \n")

        //preparing sen so that the parse tree can be constructed
        flipSen()
        sen.add(0, "<program>")
        buildTree(sen.first())

        //actually printing the parse tree
        for(i in printingList){
            println(i.toString())
        }
    }

    //this function builds the parse tree
    private fun buildTree(s : String){
       when(s){
           "<program>" -> {
               printingList.add(sen.first()) //adding <program>
               pre.addLayerBranch(sen.first())
               sen.removeFirst()
               buildTree(sen.first()) //next
           }
           "stop" -> {
               printingList.add(pre.getPrefix() + sen.first()) //adding stop
               sen.removeFirst()
               sen.add(0, "<instructions>")
               buildTree(sen.first())
           }
           "<instructions>" -> {
               //checking for multiple instructions
               if(semiColons != 0){
                   printMultipleInstruction(sen.first())
               }else {
                   printSingleInstruction(sen.first())
               }
           }
           "start" -> {
               pre.reset()
               pre.addLayer("<program>")
               printingList.add(pre.getPrefix() + sen.first())
               sen.removeFirst()
           }
       }
    }

    private fun printMultipleInstruction(s : String){
        printingList.add(pre.getPrefix() + sen.first()) //adding <instructions>
        pre.addLayerBranch("<instructions>" + pre.space)
        sen.removeFirst()

        sen.add(0, "<instruction>")
        printingList.add(pre.getPrefix() + sen.first()) //adding <instruction>
        pre.addLayerBranch(sen.first() + pre.space)
        sen.removeFirst()

        printCalculation()

        printingList.add(pre.getPrefix() + sen.first()) //adding <=
        sen.removeFirst()

        pre.removeLayer()
        pre.addLayer("<instruction>" + pre.space)
        printingList.add(pre.getPrefix() + "<variable>")
        pre.addLayer("<variable>" + pre.space)
        printingList.add(pre.getPrefix() + sen.first()) //adding the last variable
        sen.removeFirst()

        pre.removeLayer()
        pre.removeLayer()
        printingList.add(pre.getPrefix() + sen.first()) //adding ;
        sen.removeFirst() //removing ;
        semiColons--
        pre.removeLayer()
        pre.addLayer("<instructions>" + pre.space)
        sen.add(0, "<instructions>")
        buildTree(sen.first())
    }

    private fun printSingleInstruction(s: String){
        printingList.add(pre.getPrefix() + sen.first()) //adding <instructions>
        pre.addLayer("<instructions>" + pre.space)
        sen.removeFirst()

        sen.add(0, "<instruction>")
        printingList.add(pre.getPrefix() + sen.first()) //adding <instruction>
        pre.addLayerBranch(sen.first() + pre.space)
        sen.removeFirst()

        printCalculation()

        printingList.add(pre.getPrefix() + sen.first()) //adding <=
        sen.removeFirst()

        pre.removeLayer()
        pre.addLayer("<instruction>" + pre.space)
        printingList.add(pre.getPrefix() + "<variable>")
        pre.addLayer("<variable>" + pre.space)
        printingList.add(pre.getPrefix() + sen.first()) //adding the last variable
        sen.removeFirst()
        pre.removeLayer()
        pre.removeLayer()
        pre.removeLayer()
        pre.removeLayer()
        pre.addLayer("<program>")
        buildTree(sen.first())
    }

    private fun printCalculation(){
        if(sen.get(1) == "/" || sen.get(1) == "*"){ //it's a expression

            printingList.add(pre.getPrefix() + "<calculation>") //adding <calculation>
            pre.addLayerBranch("<calculation>" + pre.space)

            printingList.add(pre.getPrefix() + "<variable>") //adding <variable>
            pre.addLayer("<variable>" + pre.space)
            printingList.add(pre.getPrefix() + sen.first()) //adding the actual variable
            sen.removeFirst()

            pre.removeLayer()
            printingList.add(pre.getPrefix() + sen.first()) //adding * or /
            sen.removeFirst()

            pre.removeLayer()
            pre.addLayer("<calculation>" + pre.space)
            printingList.add(pre.getPrefix() + "<variable>") //adding <variable>
            pre.addLayer("<variable>" + pre.space)
            printingList.add(pre.getPrefix() + sen.first()) //adding the actual variable
            sen.removeFirst()

            pre.removeLayer()
            pre.removeLayer()

        } else { //it's a assignment
            printingList.add(pre.getPrefix() + "<calculation>") //adding <calculation>
            pre.addLayer("<calculation>" + pre.space)

            printingList.add(pre.getPrefix() + "<variable>") // adding variable
            pre.addLayer("<variable>" + pre.space)
            printingList.add(pre.getPrefix() + sen.first()) //adding the actual variable
            sen.removeFirst()

            pre.removeLayer()
            pre.removeLayer()
        }
    }

    //returns the amount of semicolon inside the sentence
    private fun countSemiColons(sen : MutableList<String>): Int {
        var counter : Int = 0
        for(i in sen){
            if(i == ";"){ counter++ }
        }
        return counter
    }

    //inverts sen
    private fun flipSen(){
        var nes : MutableList<String> = emptyList<String>().toMutableList()
        for(i in sen){ nes.add(0, i) }

        sen = nes
    }
}

//subclass
class prefix constructor(){
    public var layer_count : Int = 0
    private var layer : String = ""
    private var layer_branch : String = ""
    private var actual_prefix : MutableList<String> = emptyList<String>().toMutableList()
    public var space : String = "    "

    //this function adds the equal amount of whitespace as the length of the string supplied
    public fun addLayer(c_string : String){
        var length = c_string.length
        var x : Int = 0
        var temp_s = ""
        while(x != length){
            temp_s = "$temp_s "
            x++
        }
        actual_prefix.add(temp_s)
        layer_count++
    }

    public fun addLayerBranch(c_string : String){
        var length = c_string.length
        var x : Int = 1
        var temp_s = ""
        while(x != length){
            temp_s = "$temp_s "
            x++
        }
        //layer_branch = "$layer_branch|"

        actual_prefix.add("$temp_s|")
        layer_count++
    }

    //removes the last layer in the actual prefix
    public fun removeLayer(){
        actual_prefix.removeLast()
    }

    //resets everything
    public fun reset(){
        layer = ""
        layer_branch = ""
        actual_prefix = emptyList<String>().toMutableList()
        layer_count = 0
    }

    //return the prefix with '`-- ' attached in front
    public fun getPrefix(): String {
        var return_string : String = ""

        for(i in actual_prefix){
            return_string += i
        }

        //return_string = return_string.substring(0,return_string.length)
       // return_string.drop(return_string.length-1)
        return ("$return_string`-_ ")
    }
}