import BookList from "./BookList";
import SearchFilter from "./SearchFilter";

const Homepage = () => {
    return ( 
    <div className="homepage">
        <BookList />
        <SearchFilter />
    </div> );
}
 
export default Homepage;