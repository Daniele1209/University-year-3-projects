import React, {  useContext, useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router';
import {Storage} from "@capacitor/storage";
import { IonChip, 
  IonContent, 
  IonFab, 
  IonFabButton, 
  IonHeader, 
  IonIcon, 
  IonInfiniteScroll, 
  IonInfiniteScrollContent, 
  IonItem, 
  IonLabel, 
  IonList, 
  IonListHeader, 
  IonLoading, 
  IonPage, 
  IonSearchbar, 
  IonSelect, 
  IonSelectOption, 
  IonToast, 
  IonToolbar } from "@ionic/react";
import { add } from 'ionicons/icons';
import { AuthContext } from "../auth";
import Book from './Book';
import { BookProps } from "./BookProps";
import { BookContext } from './BookProvider';
import { Network } from '@capacitor/network';

const BookList: React.FC<RouteComponentProps> = ({ history }) => {
  const { logout } = useContext(AuthContext)
  const { books, fetching, fetchingError } = useContext(BookContext);
  const [disableInfiniteScroll, setDisabledInfiniteScroll] = useState<boolean>(false);
  const [visibleItems, setVisibleItems] = useState<BookProps[] | undefined>([]);
  const [page, setPage] = useState(5)
  const [filter, setFilter] = useState<string | undefined>(undefined);
  const [search, setSearch] = useState<string>("");
  const [status, setStatus] = useState<boolean>(true);
  const offset = 10;
  const {savedOffline, setSavedOffline} = useContext(BookContext);
  const genres = ["Science", "Science Fiction", "Nonfiction"];

  Network.getStatus().then(status => setStatus(status.connected));

  Network.addListener('networkStatusChange', (status) => {
      setStatus(status.connected);
  })

  useEffect(() => {
    if (books?.length && books?.length > 0) {
        setPage(offset);
        fetchData();
        console.log(books);
    }
  }, [books]);

  useEffect(() => {
      if (books && filter) {
          setVisibleItems(books.filter(each => each.genre === filter));
      }
  }, [filter]);

  useEffect(() => {
      if (search === "") {
          setVisibleItems(books);
      }
      if (books && search !== "") {
          setVisibleItems(books.filter(each => each.title.startsWith(search)));
      }
  }, [search]);

  function fetchData() {
      setVisibleItems(books?.slice(0, page + offset));
      setPage(page + offset);
      if (books && page > books?.length) {
          setDisabledInfiniteScroll(true);
          setPage(books.length);
      } else {
          setDisabledInfiniteScroll(false);
      }
  }

  async function searchNext($event: CustomEvent<void>) {
    fetchData();
    ($event.target as HTMLIonInfiniteScrollElement).complete();
}

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonItem>
                <IonSelect style={{ width: '30%' }} value={filter} placeholder="Pick a genre" onIonChange={(e) => setFilter(e.detail.value)}>
                    {genres.map((each) => (
                        <IonSelectOption key={each} value={each}>
                                {each}
                        </IonSelectOption>
                    ))}
                </IonSelect>
                <IonSearchbar style={{ width: '45%' }} placeholder="Search by title" value={search} debounce={200} onIonChange={(e) => {
                    setSearch(e.detail.value!);
                }}>
                </IonSearchbar>
                <IonChip>
                <IonLabel color={status? "success" : "danger"}>{status? "Online" : "Offline"}</IonLabel>
            </IonChip>
            </IonItem>

        </IonToolbar>
      </IonHeader>
      <IonContent fullscreen>
        <IonLoading isOpen={fetching} message="Fetching books ..." />
        {visibleItems && (
          <IonList>
            <IonListHeader>
                <IonLabel>ID</IonLabel>  
                <IonLabel>Title</IonLabel>
                <IonLabel>Author</IonLabel>
                <IonLabel>Genre</IonLabel>
                <IonLabel>Latitude</IonLabel>
                <IonLabel>Longitude</IonLabel>
                <IonLabel>Image</IonLabel>

            </IonListHeader>
            {Array.from(visibleItems)
                .filter(each => {
                    if (filter !== undefined)  
                        return each.genre === filter && each._id !== undefined;
                    return each._id !== undefined;
                })
                .map(({_id, title, author, genre, latitude, longitude, image}) => 
                <Book key={_id} _id={_id} title={title} author={author} genre={genre} latitude={latitude} longitude={longitude} image={image} onEdit={_id => history.push(`/api/elements/book/${_id}`)} />)}
        </IonList>
        )}
        <IonInfiniteScroll threshold="100px" disabled={disableInfiniteScroll} onIonInfinite={(e: CustomEvent<void>) => searchNext(e)}>
            <IonInfiniteScrollContent loadingText="Loading...">
            </IonInfiniteScrollContent>
        </IonInfiniteScroll>
        {fetchingError && (
          <div>{fetchingError.message || 'Failed to fetch books'}</div>
        )}
        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => history.push('/api/elements/book')}>
            <IonIcon icon={add} />
          </IonFabButton>
        </IonFab>

        <IonFab vertical="bottom" horizontal="start" slot="fixed">
                    <IonFabButton onClick={handleLogout}>
                        Logout
                    </IonFabButton>
                </IonFab>
        <IonToast
            isOpen={savedOffline ? true : false}
            message="Your changes will be visible on server when you get back online!"
            duration={2000}/>
      </IonContent>
    </IonPage>
  );

  function handleLogout() {
    Storage.clear().then(() => console.log("Storage cleared"));
    logout?.();
    history.push("/login");
}
};

export default BookList;
