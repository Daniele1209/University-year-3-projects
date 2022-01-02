using Lab4.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Lab4.Parsers
{
    class TaskImplementation
    {
        private static List<string> hostList;

        public static void run(List<string> hostnames, bool async)
        {
            hostList = hostnames;
            var taskList = new List<Task>();

            for (var idx = 0; idx < hostnames.Count; idx++)
            {
                if (async)
                    taskList.Add(Task.Factory.StartNew(AsyncStart, idx));
                else
                    taskList.Add(Task.Factory.StartNew(Start, idx));
            }
            Task.WaitAll(taskList.ToArray());
        }

        private static void AsyncStart(object idObject)
        {
            var id = (int)idObject;
            AsyncClient(hostList[id], id);
        }

        private static void Start(object idObject)
        {
            var id = (int)idObject;
            NormalClient(hostList[id], id);
        }

        // -------------------- NORMAL CLIENT --------------------

        private static void NormalClient(string host, int id)
        {
            var IPHost = Dns.GetHostEntry(host.Split('/')[0]); 
            var IPAddress = IPHost.AddressList[0];
            var remEndPoint = new IPEndPoint(IPAddress, Parser.PORT); 
            // Socket creation
            var client = new Socket(IPAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            var requestSocket = new Custom_Socket
            {
                sock = client,
                hostname = host.Split('/')[0],
                endpoint = host.Contains("/") ? host.Substring(host.IndexOf("/", StringComparison.Ordinal)) : "/",
                remoteEndPoint = remEndPoint,
                id = id
            };

            // Connection to the remote server
            Connect(requestSocket).Wait();
            // Data request
            Send(requestSocket, Parser.StringRequest(requestSocket.hostname, requestSocket.endpoint)).Wait();
            // Server resonse
            Receive(requestSocket).Wait(); 

            Console.WriteLine("connect -> {0} with length of -> {1}", requestSocket.id, Parser.GetLength(requestSocket.responseContent.ToString()));

            // Socket release
            client.Shutdown(SocketShutdown.Both);
            client.Close();
        }

        private static Task Connect(Custom_Socket state)
        {
            // Gets blocked until it is signaled
            state.sock.BeginConnect(state.remoteEndPoint, ConnectCallback, state);
            return Task.FromResult(state.connectDone.WaitOne());
        }

        private static Task Send(Custom_Socket state, string data)
        {
            // String -> byte using ASCII
            var dataByte = Encoding.ASCII.GetBytes(data);

            state.sock.BeginSend(dataByte, 0, dataByte.Length, 0, SendCallback, state);
            return Task.FromResult(state.sendDone.WaitOne());
        }

        private static Task Receive(Custom_Socket state)
        {
            state.sock.BeginReceive(state.buffer, 0, Custom_Socket.BUFF_SIZE, 0, ReceiveCallback, state);
            return Task.FromResult(state.receiveDone.WaitOne());
        }

        // -------------------- ASYNC CLIENT --------------------

        private static async void AsyncClient(string host, int id)
        {
            var IPHost = Dns.GetHostEntry(host.Split('/')[0]);
            var IPAddress = IPHost.AddressList[0];
            var remoteEndpoint = new IPEndPoint(IPAddress, Parser.PORT);

            // TCP/IP socket creation (client socket)
            var client = new Socket(IPAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            var requestSocket = new Custom_Socket
            {
                sock = client,
                hostname = host.Split('/')[0],
                endpoint = host.Contains("/") ? host.Substring(host.IndexOf("/", StringComparison.Ordinal)) : "/",
                remoteEndPoint = remoteEndpoint,
                id = id
            };

            // Connection to the remote server
            await ConnectAsync(requestSocket);
            // Data request
            await SendAsync(requestSocket, Parser.StringRequest(requestSocket.hostname, requestSocket.endpoint));
            // Server resonse
            await ReceiveAsync(requestSocket);

            Console.WriteLine("connect -> {0} with length of -> {1}", requestSocket.id, Parser.GetLength(requestSocket.responseContent.ToString()));

            // Socket release
            client.Shutdown(SocketShutdown.Both);
            client.Close();
        }

        private static async Task ConnectAsync(Custom_Socket state)
        {
            state.sock.BeginConnect(state.remoteEndPoint, ConnectCallback, state);
            await Task.FromResult<object>(state.connectDone.WaitOne()); 
        }

        private static async Task SendAsync(Custom_Socket state, string data)
        {
            var byteData = Encoding.ASCII.GetBytes(data);

            state.sock.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, state);
            await Task.FromResult<object>(state.sendDone.WaitOne());
        }

        private static async Task ReceiveAsync(Custom_Socket state)
        {
            state.sock.BeginReceive(state.buffer, 0, Custom_Socket.BUFF_SIZE, 0, ReceiveCallback, state);
            await Task.FromResult<object>(state.receiveDone.WaitOne());
        }

        // -------------------- CALLBACKS --------------------

        private static void ConnectCallback(IAsyncResult asy_res)
        {
            // Details from connection
            var resultedSock = (Custom_Socket)asy_res.AsyncState;
            var clientSock = resultedSock.sock;
            var clientID = resultedSock.id;
            var hostName = resultedSock.hostname;
            // End Conenction
            clientSock.EndConnect(asy_res);
            Console.WriteLine("socket connected {1} - {2}", clientID, hostName, clientSock.RemoteEndPoint);
            resultedSock.connectDone.Set();
        }


        private static void SendCallback(IAsyncResult asy_res)
        {
            var resultedSock = (Custom_Socket)asy_res.AsyncState;
            var clientSock = resultedSock.sock;
            var clientID = resultedSock.id;
            // Ending data sending to the server
            var bytesSent = clientSock.EndSend(asy_res);
            Console.WriteLine("connect -> {0} sending {1} bytes to the server", clientID, bytesSent);
            // Signal when all the data bytes are sent
            resultedSock.sendDone.Set();
        }

        private static void ReceiveCallback(IAsyncResult asy_res)
        {
            var resultedSock = (Custom_Socket)asy_res.AsyncState;
            var clientSock = resultedSock.sock;
            try
            {
                // Try to read data
                var bytesRead = clientSock.EndReceive(asy_res);
                // Get data from buffer
                // Store the data into resultedSock.responseContent
                resultedSock.responseContent.Append(Encoding.ASCII.GetString(resultedSock.buffer, 0, bytesRead));
                //Check if the response was fetched or not
                if (!Parser.HeaderResponse(resultedSock.responseContent.ToString()))
                    clientSock.BeginReceive(resultedSock.buffer, 0, Custom_Socket.BUFF_SIZE, 0, ReceiveCallback, resultedSock);
                else
                    // We signal when all the bytes have been received
                    resultedSock.receiveDone.Set();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

        }

    }
}