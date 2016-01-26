package com.example.removeBlanks

object RemoveBlanks {
    def apply(path: String, comressWhiteSpace: Boolean = false): Seq[String] =
        for {
            line <- scala.io.Source.fromFile(path).getLines.toSeq
            if line.matches("""^\s*$""") == false
            line2 = if (comressWhiteSpace) line replaceAll("\\s+", " ")
                    else line
        } yield line2

    def main(args: Array[String]) = for {
        path2 <- args
        (compress, path) = if (path2 startsWith "-") (true, path2.substring(1))
                            else (false, path2)
        line <- apply(path, compress)
    } println(line
    )
}
