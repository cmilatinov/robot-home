export function convert(arr) {
    if (!arr)
        return [];
    let size = arr.size();
    let result = [];
    for (let i = 0; i < size; i++)
        result.push(arr.get(i));
    return result;
}