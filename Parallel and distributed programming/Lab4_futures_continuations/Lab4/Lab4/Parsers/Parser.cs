using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab4.Parsers
{
    class Parser
    {
        public static int PORT = 80;
        public static string StringRequest(string hostName, string endPoint)
        {
            return "GET request -> " + endPoint + " HTTP/1.1\r\n" + "Host -> " + hostName + "\r\n" + "Length -> 0\r\n\r\n";
        }

        public static bool HeaderResponse(string response)
        {
            return response.Contains("\r\n\r\n");
        }

        public static int GetLength(string respContent)
        {
            var lenght = 0;
            var response = respContent.Split('\r', '\n');
            foreach (string responseLine in response)
            {
                var header = responseLine.Split(':');
                if (String.Compare(header[0], "Content-Length", StringComparison.Ordinal) == 0)
                    lenght = int.Parse(header[1]);
            }

            return lenght;
        }
    }
}