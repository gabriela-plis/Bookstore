import { useState } from "react";
import BookList from "./BookList";
import SearchFilter from "./SearchFilter";

type Props = {
    sessionIsActive: boolean;
}

const Homepage = (props: Props) => {
    return ( 
    <main className="homepage">
        {!props.sessionIsActive && <p className="logout-text">You have successfully been logged out! Have a nice day</p>}
        <BookList url='http://localhost:8000/books' isBorrowBtn={true} isReturnBtn={false} />
        <SearchFilter />
    </main> 
    );
}
 
export default Homepage;