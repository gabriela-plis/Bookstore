interface Book {
    readonly ID: number,
    title: string,
    author: string,
    publishYear: number,
    availableAmount: number, 
    type: string
}
 
export default Book;