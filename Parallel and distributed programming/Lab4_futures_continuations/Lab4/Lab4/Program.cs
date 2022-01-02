using System;
using System.Collections.Generic;
using System.Linq;
using Lab4.Parsers;

namespace Lab4
{
    class Program
    {
        static void Main(string[] args)
        {
            var url_list = new string[] {
                "www.cs.ubbcluj.ro/~motogna/LFTC",
                "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/",
                "www.cs.ubbcluj.ro/~forest"
            }.ToList();

            // IMPLEMENTATIONS

            //CallbacksImplementation.run(url_list);
            TaskImplementation.run(url_list, true);
        }
    }
}
