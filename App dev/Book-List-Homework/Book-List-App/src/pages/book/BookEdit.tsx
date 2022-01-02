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
  IonDatetime,
  IonItem,
  IonLabel,
  IonSelect,
  IonSelectOption
} from '@ionic/react';
import { getLogger } from '../../core';
import { BookContext } from './BookProvider';
import { RouteComponentProps } from 'react-router';
import { BookProps } from './BookProps';
import { MyMap } from '../../core/MyMap';
import { useMyLocation } from '../../core/useMyLocation';
import { usePhotoGallery } from '../../core/usePhotoGallery';
import { defineCustomElements } from '@ionic/pwa-elements/loader';


interface BookEditProps extends RouteComponentProps<{
  id?: string;
}> {}

defineCustomElements(window);

const BookEdit: React.FC<BookEditProps> = ({ history, match }) => {
  const { books, saving, savingError, saveBook } = useContext(BookContext);
  const [title, setTitle] = useState<string>('');
  const [author, setAuthor] = useState<string>('');
  const [genre, setGenre] = useState<string>('');
  const [book, setBook] = useState<BookProps>();
  const [image, setImage] = useState('');
  const [latitude, setLatitude] = useState<number | undefined>(undefined);
  const [longitude, setLongitude] = useState<number | undefined>(undefined);
  const [currentLatitude, setCurrentLatitude] = useState<number | undefined>(undefined);
  const [currentLongitude, setCurrentLongitude] = useState<number | undefined>(undefined);
  
  const location = useMyLocation();
  const {latitude : lat, longitude : lng} = location.position?.coords || {};

  const {takePhoto} = usePhotoGallery();

  useEffect(() => {
    const routeId = match.params.id || '';
    const book = books?.find(it => it._id === routeId);
    setBook(book);
    if (book) {
      setTitle(book.title);
      setAuthor(book.author);
      setGenre(book.genre);
      setLatitude(book.latitude);
      setLongitude(book.longitude);
      setImage(book.image);
    }
  }, [match.params.id, books]);

  useEffect(() => {
    if (latitude === undefined && longitude === undefined) {
        setCurrentLatitude(lat);
        setCurrentLongitude(lng);
    } else {
        setCurrentLatitude(latitude);
        setCurrentLongitude(longitude);
    }
  }, [lat, lng, longitude, latitude]);
  
  const handleSave = () => {
    const edited_book = book ? {...book, title, author, genre, latitude: latitude, longitude: longitude, image: image } : { title, author, genre, latitude: latitude, longitude: longitude, image: image };
    console.log(edited_book);
    saveBook && saveBook(edited_book).then(() => {history.goBack()});
  };

  async function handlePhotoChange() {
    const image = await takePhoto();
    if (!image) {
        setImage('');
    } else {
        setImage(image);
    }
  }

function setLocation() {
    setLatitude(currentLatitude);
    setLongitude(currentLongitude);
}

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
        <IonItem>
          <IonLabel>Title</IonLabel>
          <IonInput value={title} onIonChange={e => setTitle(e.detail.value || '')} />
        </IonItem>
        <IonItem>
          <IonLabel>Author</IonLabel>
          <IonInput value={author} onIonChange={e => setAuthor(e.detail.value || '')} />
        </IonItem>
        <IonItem>
          <IonLabel>Genre</IonLabel>    
            <IonSelect value={genre} onIonChange={e => setGenre(e.detail.value)}>
              <IonSelectOption value="Science">Science</IonSelectOption>
              <IonSelectOption value="Nonfiction">Nonfiction</IonSelectOption>
              <IonSelectOption value="Science Fiction">Science Fiction</IonSelectOption>
            </IonSelect>
        </IonItem>
        <IonItem>
            <IonLabel>Location of where you got the book from: </IonLabel>
            <IonButton onClick={setLocation}>Set location</IonButton>
        </IonItem>
        {image && (<img onClick={handlePhotoChange} src={image} width={'100px'} height={'100px'}/>)}
        {!image && (<img onClick={handlePhotoChange} src={'https://static.thenounproject.com/png/187803-200.png'} width={'100px'} height={'100px'}/>)}

        {lat && lng &&
            <MyMap
                lat={currentLatitude}
                lng={currentLongitude}
                onMapClick={log('onMap')}
                onMarkerClick={log('onMarker')}
            />
        }
        <IonLoading isOpen={saving} />
        {savingError && (
          <div>{savingError?.message || 'Failed to save book !'}</div>
        )}
      </IonContent>
    </IonPage>
  );
  function log(source: string) {
    return (e: any) => {
    setCurrentLatitude(e.latLng.lat());
    setCurrentLongitude(e.latLng.lng());
    console.log(source, e.latLng.lat(), e.latLng.lng());
    }
}
};

export default BookEdit;
