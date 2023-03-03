import { useEffect, useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList from "../../reusable-components/BookList";
import Popup from "../../reusable-components/Popup";
import SearchFilter from "./SearchFilter";

type Props = {
    displayLogoutText: boolean;
}

const Homepage = (props: Props) => {

    const [isOperationActive, setIsOperationActive] = useState(false);
    const [bookId, setBookId] = useState(0);

    const handleClose = () => {
        const text = document.querySelector(".logout-text")!;
        text.remove();
    }

    const handleBorrow = () => {
        //not logged in
        //you already have this book borrowed
        //borrow
    }

    return ( 
    <main className="homepage">
        {props.displayLogoutText && 
            <p className="logout-text" key={'logout-text'}>You have successfully been logged out! Have a nice day 
                <button className="btn btn--smaller btn--greater-border-radius btn--margin" onClick={handleClose}>x</button>
            </p>
        }
        <BookList url='http://localhost:8000/books' operationType={OperationTypes.Borrow} setBookId={setBookId} handleOperation={handleBorrow} />
        <SearchFilter />
    </main> 
    );
}
 
export default Homepage;