import BookList from "./BookList";
import SearchFilter from "./SearchFilter";

const Homepage = () => {
    return ( 
    <main className="homepage">
        <BookList />
        <SearchFilter />
    </main> 
    );
}
 
export default Homepage;