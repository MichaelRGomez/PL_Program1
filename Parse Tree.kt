//actual class
class parseTree  constructor(formatted_string : List<String>){
    private var sen : MutableList<String> = formatted_string.toMutableList()
    private var semicolons : Int = countSemiColons()
    private var pre : prefix = prefix()
    private val lexemses : List<String> = listOf("start", "stop", "R", "T", "S", "U", ";", "<=", "/", "*")
    private var printinList : MutableList<String> = emptyList<String>().toMutableList()

    public fun print(){
        printTree()
    }

    private fun printTree(){
        if(sen.isNotEmpty()){
            when(sen.first()){
                "start" -> printProgram(sen.first())
                "<instructions>" -> printInstructions(sen.first())
            }
        }
    }

    private fun printInstructions(s : String){
        if (semicolons > 0){}
    }

    private fun printProgram(s : String){
        println("<program>")
        pre.addLayerBranch()
        println(pre.getPrefix() + s)
        sen.removeFirst()
        sen.add("<instructions>")
        printTree()

    }

    private fun countSemiColons(): Int {
        var counter : Int = 0
        for (i in sen){
            if(i == ";") { counter++ }
        }
        return counter
    }
}

//subclass
class prefix constructor(){
    public var layer_count : Int = 0
    private var layer : String = "     "
    private var layer_branch = "    |"
    private var actual_prefix : MutableList<String> = emptyList<String>().toMutableList()

    public fun addLayer(){
        actual_prefix.add(layer)
        layer_count++
    }

    public fun addLayerBranch(){
        actual_prefix.add(layer_branch)
        layer_count++
    }

    public fun removeLayer(){
        actual_prefix.removeLast()
    }

    public fun reset(){
        actual_prefix = emptyList<String>().toMutableList()
        layer_count = 0
    }

    public fun getPrefix(): String {
        return ("$actual_prefix`-- ")
    }
}