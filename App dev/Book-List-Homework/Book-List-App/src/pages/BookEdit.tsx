import React, { useContext, useEffect, useState } from 'react';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonInput,
  IonLoading,
  IonPage,
  IonTitle,
  IonToolbar,
  IonDatetime
} from '@ionic/react';
import { getLogger } from '../core';
import { BookContext } from './BookProvider';
import { RouteComponentProps } from 'react-router';
import { BookProps } from './BookProps';


interface BookEditProps extends RouteComponentProps<{
  id?: string;
}> {}

const BookEdit: React.FC<BookEditProps> = ({ history, match }) => {
  const { books, saving, savingError, saveBook } = useContext(BookContext);
  const [title, setTitle] = useState<string>('');
  const [author, setAuthor] = useState<string>('');
  const [description, setDescription] = useState<string>('');
  const [published, setPublished] = useState<Date>(new Date());
  const [book, setBook] = useState<BookProps>();
  useEffect(() => {
    const routeId = match.params.id || 0;
    const book = books?.find(it => it.id === routeId);
    setBook(book);
    if (book) {
      setTitle(book.title);
      setAuthor(book.author);
      setDescription(book.description);
      setPublished(book.published);
    }
  }, [match.params.id, books]);
  
  const handleSave = () => {
    const editedBook = book ? { ...book, 
      title,
      author,
      description,
      published} : { title, author, description, published };
    saveBook && saveBook(editedBook).then(() => history.goBack());
  };

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>Edit Book</IonTitle>
          <IonButtons slot="end">
            <IonButton onClick={handleSave}>
              Save
            </IonButton>
          </IonButtons>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonInput value={title} onIonChange={e => setTitle(e.detail.value || '')} />
        <IonInput value={author} onIonChange={e => setAuthor(e.detail.value || '')} />
        <IonInput value={description} onIonChange={e => setDescription(e.detail.value || '')} />
        <IonDatetime displayFormat={"DD-MM-YYYY"} value={published.toLocaleString()} onIonChange={e => setPublished(new Date(e.detail.value || new Date()))}/>
        <IonLoading isOpen={saving} />
        {savingError && (
          <div>{savingError.message || 'Failed to save book !'}</div>
        )}
      </IonContent>
    </IonPage>
  );
};

export default BookEdit;
