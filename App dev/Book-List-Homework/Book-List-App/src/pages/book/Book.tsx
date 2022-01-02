import React, { useEffect, useState } from 'react';
import { IonItem, IonLabel, createAnimation, IonModal } from '@ionic/react';
import { BookProps } from './BookProps';

interface BookPropsExt extends BookProps {
  onEdit: (_id?: string) => void;
}

const Book: React.FC<BookPropsExt> = ({ _id, title, author, genre, latitude, longitude, image, onEdit }) => {

  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    document.getElementById("book")!.addEventListener('click', () => {
        onEdit(_id);
    });
  }, [document.getElementById("book")]);

  useEffect(() => {
    document.getElementById("picture")!.addEventListener('mouseenter', () => {
        setShowModal(true);
    });
  }, [document.getElementById("picture")]);


const enterAnimation = (baseEl: any) => {

  const backdropAnimation = createAnimation()
      .addElement(baseEl.querySelector('ion-backdrop')!)
      .fromTo('opacity', '0.01', 'var(--backdrop-opacity)');

  const wrapperAnimation = createAnimation()
      .addElement(baseEl.querySelector('.modal-wrapper')!)
      .keyframes([
          { offset: 0, opacity: '0', transform: 'scale(0)' },
          { offset: 1, opacity: '0.99', transform: 'scale(1)' }
      ]);

  return createAnimation()
      .addElement(baseEl)
      .easing('ease-out')
      .duration(500)
      .addAnimation([backdropAnimation, wrapperAnimation]);
}

const leaveAnimation = (baseEl: any) => {
  return enterAnimation(baseEl).direction('reverse');
}

return (
    <IonItem id="book">
      <IonLabel>{_id}</IonLabel>
      <IonLabel>{title}</IonLabel>
      <IonLabel>{author}</IonLabel>
      <IonLabel>{genre}</IonLabel>
      <IonLabel>{latitude}</IonLabel>
      <IonLabel>{longitude}</IonLabel>
      {image && (<img id="picture" src={image} width={'100px'} height={'100px'} onClick={() => {
          setShowModal(true);
      }} />)}
      <IonModal isOpen={showModal} enterAnimation={enterAnimation} leaveAnimation={leaveAnimation} backdropDismiss={true} onDidDismiss={() => setShowModal(false)}>
        <img id="picture" src={image} />
      </IonModal>

      {!image && (<img src={'https://static.thenounproject.com/png/187803-200.png'} width={'100px'} height={'100px'} />)}
    </IonItem>
  )
};

export default Book;
