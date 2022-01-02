import axios from 'axios';
import { getLogger, authConfig, withLogs } from '../../core';
import { BookProps } from './BookProps';
import { Storage } from '@capacitor/storage';

const log = getLogger('bookApi');
const url = 'localhost:3000';
const baseUrl = `http://${url}/api/elements`;

interface MessageData {
  type: string;
  payload: BookProps;
}


const different = (book1: any, book2: any) => {
  if (book1.title === book2.title && book1.author === book2.author && book1.genre === book2.genre && book1.published === book2.published)
    return false;
  return true;
}

export const syncData: (token: string) => Promise<BookProps[]> = async token => {
  try {
    const { keys } = await Storage.keys();
    var result:any = axios.get(`${baseUrl}/books`, authConfig(token));
    result.then(async result => {
      keys.forEach(async (i:any) => {
        if (i !== 'token') {
          const bookOnServer = result.data.find((each: { _id: string; }) => each._id === i);
          const bookLocal = await Storage.get({key: i});

          console.log('BOOK ON SERVER: ' + JSON.stringify(bookOnServer));
          console.log('BOOK LOCALLY: ' + bookLocal.value!);

          if (bookOnServer !== undefined && different(bookOnServer, JSON.parse(bookLocal.value!))) {  // actualizare
            console.log('UPDATE ' + bookLocal.value);
            axios.put(`${baseUrl}/book/${i}`, JSON.parse(bookLocal.value!), authConfig(token));
          } else if (bookOnServer === undefined){  // creare
            console.log('CREATE' + bookLocal.value!);
            axios.post(`${baseUrl}/book`, JSON.parse(bookLocal.value!), authConfig(token));
          }
        }
        })
    }).catch(err => {
      if (err.response) {
        console.log('client received an error response (5xx, 4xx)');
      } else if (err.request) {
        console.log('client never received a response, or request never left');
      } else {
        console.log('anything else');
      }
  })
    return withLogs(result, 'syncBooks');
  } catch (error) {
    throw error;
  }    
}

export const getBooks: (token: string) => Promise<BookProps[]> = token => {
  try {
    var result:any = axios.get(`${baseUrl}/books`, authConfig(token));
    result.then(async result => {
      for (const each of result.data) {
          await Storage.set({
            key: each._id!,
            value: JSON.stringify({
              _id: each._id,
              title: each.title,
              author: each.author,
              genre: each.genre,
              latitude: each.latitude,
              longitude: each.longitude,
              image: each.image
            })
          });
      }
    }).catch(err => {
      if (err.response) {
        console.log('client received an error response (5xx, 4xx)');
      } else if (err.request) {
        console.log('client never received a response, or request never left');
      } else {
        console.log('anything else');
      }
  })
    return withLogs(result, 'getItems');
  } catch (error) {
    throw error;
  }    
}

export const createBook: (token: string, book: BookProps) => Promise<BookProps[]> = (token,book) => {
  var result:any = axios.post(`${baseUrl}/book`, book, authConfig(token));
    result.then(async result => {
      var one = result.data;
      await Storage.set({
        key: one._id!,
        value: JSON.stringify({
          _id: one._id,
          title: one.title,
          author: one.author,
          genre: one.genre,
          latitude: one.latitude,
          longitude: one.longitude,
          image: one.image
          })
      });
    }).catch(err => {
      if (err.response) {
        console.log('client received an error response (5xx, 4xx)');
      } else if (err.request) {
        alert('client never received a response, or request never left');
      } else {
        console.log('anything else');
      }
  });
    return withLogs(result, 'createItem');
}

export const updateBook: (token: string, book: BookProps) => Promise<BookProps[]> = (token,book) => {
  var result:any = axios.put(`${baseUrl}/book/${book._id}`, book, authConfig(token));
    result.then(async result => {
      var one = result.data;
      await Storage.set({
        key: one._id!,
        value: JSON.stringify({
          _id: one._id,
          title: one.title,
          author: one.author,
          genre: one.genre,
          latitude: one.latitude,
          longitude: one.longitude,
          image: one.image
          })
      }).catch(err => {
        if (err.response) {
          alert('client received an error response (5xx, 4xx)');
        } else if (err.request) {
          alert('client never received a response, or request never left');
        } else {
          alert('anything else');
        }
    })
    });
    return withLogs(result, 'updateItem');
}

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
  const ws = new WebSocket(`ws://${url}`);
  ws.onopen = () => {
    log('web socket onopen');
    ws.send(JSON.stringify({ type: 'authorization', payload: { token } }));
  };
  ws.onclose = function (event) {
    console.log(event);
    log('web socket onclose');
  };
  ws.onerror = error => {
    log('web socket onerror', error);
    ws.close();
  };
  ws.onmessage = messageEvent => {
    log('web socket onmessage');
    onMessage(JSON.parse(messageEvent.data));
  };
  return () => {
    ws.close();
  }
}
