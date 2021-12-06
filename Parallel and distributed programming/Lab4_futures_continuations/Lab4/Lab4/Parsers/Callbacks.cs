using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Lab4.Entities;

namespace Lab4.Parsers
{
    class CallbacksImplementation
    {
    
        public static void run(List<string> hostList)
        {
            for (var idx = 0; idx < hostList.Count; idx++)
            {
                Start(hostList[idx], idx);
                Thread.Sleep(1000);
            }
        }

        private static void Start(string host, int id)
        {
            // Get the host DNS, separate the IP and create the endpoint
            var IPHost = Dns.GetHostEntry(host.Split('/')[0]);
            var IPAddress = IPHost.AddressList[0];
            var remoteEndpoint = new IPEndPoint(IPAddress, Parser.PORT);
            // Socket creation
            var client = new Socket(IPAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            var requestSocket = new Custom_Socket
            {
                sock = client,
                hostname = host.Split('/')[0],
                endpoint = host.Contains("/") ? host.Substring(host.IndexOf("/")) : "/",
                remoteEndPoint = remoteEndpoint,
                id = id
            };
            // Connection to the endpoint
            requestSocket.sock.BeginConnect(requestSocket.remoteEndPoint, Connected, requestSocket); 
        }

        private static void Connected(IAsyncResult asy_res)
        {
            var resultedSock = (Custom_Socket)asy_res.AsyncState;
            var clientSock = resultedSock.sock;
            var clientID = resultedSock.id;
            var hostName = resultedSock.hostname;
            // End Conenction
            clientSock.EndConnect(asy_res);
            Console.WriteLine("socket {0} connected {1} - {2}", clientID, hostName, clientSock.RemoteEndPoint);
            var data = Encoding.ASCII.GetBytes(Parser.StringRequest(resultedSock.hostname, resultedSock.endpoint));
            resultedSock.sock.BeginSend(data, 0, data.Length, 0, Sent, resultedSock);
        }

        private static void Sent(IAsyncResult asy_res)
        {
            var resultedSock = (Custom_Socket)asy_res.AsyncState;
            var clientSock = resultedSock.sock;
            var clientID = resultedSock.id;
            // Sending data
            var bytesSent = clientSock.EndSend(asy_res);
            Console.WriteLine("connect -> {0} sending {1} bytes to the server", clientID, bytesSent);
            // Response from server
            resultedSock.sock.BeginReceive(resultedSock.buffer, 0, Custom_Socket.BUFF_SIZE, 0, Receiving, resultedSock);
        }

        private static void Receiving(IAsyncResult asy_res)
        {
            // Receive the answer
            var resultedSock = (Custom_Socket)asy_res.AsyncState;
            var clientSock = resultedSock.sock;
            var clientID = resultedSock.id;
            try
            {
                // Try to read data
                var bytesRead = clientSock.EndReceive(asy_res);
                resultedSock.responseContent.Append(Encoding.ASCII.GetString(resultedSock.buffer, 0, bytesRead));
                //Check if the response was fetched or not
                if (!Parser.HeaderResponse(resultedSock.responseContent.ToString()))
                    clientSock.BeginReceive(resultedSock.buffer, 0, Custom_Socket.BUFF_SIZE, 0, Receiving, resultedSock);
                else
                {                    
                    Console.WriteLine("length -> {0}", Parser.GetLength(resultedSock.responseContent.ToString()));
                    // Freeing socket 
                    clientSock.Shutdown(SocketShutdown.Both);
                    clientSock.Close();                   
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
    }
}