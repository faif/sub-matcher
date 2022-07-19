(ns sub-matcher.core
  (:gen-class)
  (:import java.io.File))

(use '[clojure.string :only (ends-with? includes? last-index-of)]
     '[clojure.set :only (join)])
 
(defn has-suffix? [filepath suffix-seq]
  {:pre [(not-empty suffix-seq)]}
  "true when filepath ends with any of the suffixes in suffix-seq"
  (not
   (empty?
    (filter #(ends-with? filepath %) suffix-seq))))

(defn has-subtitle-suffix? [filepath]
  "true when filepath has a subtitle suffix"
  (has-suffix? filepath #{".sub" ".srt" ".txt"}))

(defn is-file-type? [filepath filefn]
  "true when filefn evaluates filepath as truthy"
  (and
   (.isFile ^java.io.File filepath)
   (filefn filepath)))

(defn is-subtitle-file? [filepath]
  "true when filepath points to a subtitle file"
  (is-file-type? filepath has-subtitle-suffix?))

(defn files-of-type [dirpath filterfn msg]
  "get a lazy-seq of File objects found in dirpath"
  (let [files
        (filter filterfn
                (file-seq (clojure.java.io/file dirpath)))]
    (if (empty? files)
      (str "Couldn't find a suitable " msg " file in " dirpath)
      files)))

(defn subtitle-files [dirpath]
  "get a lazy-seq of subtitle files found in dirpath"
  (files-of-type dirpath is-subtitle-file? "subtitle"))

(defn has-video-suffix? [filepath]
  "true when filepath has a video suffix"
  (has-suffix? filepath #{".avi" ".mkv" ".mp4" ".mpeg" ".mpg"}))

(defn is-video-file? [filepath]
  "true when filepath points to a video file"
  (is-file-type? filepath has-video-suffix?))

(defn video-files [dirpath]
  "get a lazy-seq of subtitle files found in dirpath"
  (files-of-type dirpath is-video-file? "video"))

(defn parent-dir [lazy-file-seq]
  "get a lazy-seq of the parent dirs of the files in lazy-file-seq"
  (map #(.getParentFile ^java.io.File %) lazy-file-seq))

(defn parent-parent-dir [lazy-file-seq]
  "get the directories two levels above the files in lazy-file-seq"
  (parent-dir (parent-dir lazy-file-seq)))

(defn parent-dir-path [file-obj]
  "get the path of the dir containing file-obj"
  (let [par (.getParentFile ^java.io.File file-obj)
        path (.getPath ^java.io.File par)]
    path))

(defn parent-parent-dir-path [file-obj]
  "get the path two dirs above file-obj"
  (let [par (.getParentFile ^java.io.File file-obj)
        parpar (.getParentFile ^java.io.File par)
        path (.getPath ^java.io.File parpar)]
    path))

(defn remove-extension [filename]
  "remove everything after the last dot of filename"
  (apply str
   (take
    (last-index-of filename ".") filename)))

(defn path-noext [file-obj]
  "hello.mp4 becomes hello"
  (remove-extension (.getPath ^java.io.File file-obj)))

(defn obj-and-join-key [file-obj file-keyword joinkeyfn]
  "get a map of {:file-keyword File :join-key /common/path}"
  {file-keyword file-obj :join-key (joinkeyfn file-obj)})

(defn files-with-joinkey [lazy-file-seq file-keyword joinkeyfn]
  "execute obj-and-join-key on lazy-file-seq"
  (map #(obj-and-join-key % file-keyword joinkeyfn) lazy-file-seq))

(defn subtitle-files-with-joinkey [lazy-file-seq]
  "get a map of {:join-key /prefix/path/to/subtitle :subfile File}"
  (files-with-joinkey lazy-file-seq :subfile parent-dir-path))

(defn video-files-with-joinkey [lazy-file-seq]
  "get a map of {:join-key /prefix/path/to/video :videofile File}"
  (files-with-joinkey lazy-file-seq :videofile path-noext))

(defn add-renamed-subtitle [file-map]
  "'extend' file-map with {:subfiledst /path/of/renamed/subtitle}"
  (let [video-file (:videofile file-map)
        subt-destination (.getParent ^java.io.File video-file)
        vid-file-noext (remove-extension (.getName ^java.io.File video-file))
        subt-filename (.getName ^java.io.File (:subfile file-map))
        subt-file-noext (remove-extension subt-filename)
        new-subt-file (clojure.string/replace subt-filename subt-file-noext vid-file-noext)
        new-subt-dest (str subt-destination java.io.File/separator new-subt-file)]
    (assoc file-map :subfiledst new-subt-dest)))

(defn renamed-subtitles [lazy-file-seq]
  "execute add-renamed-subtitle on lazy-file-seq"
  (map #(add-renamed-subtitle %) lazy-file-seq))

(defn rename-subfile-obj [src-file-obj dst-file-obj]
  "rename (and possibly also move) src-file-obj to dst-file-obj"
  (.renameTo ^java.io.File src-file-obj dst-file-obj))

(defn rename-subfile [file-map]
  "execute rename-subfile-obj with {:subfile :newsubfiledst} on file-map"
  (let [{:keys [subfile subfiledst]} file-map
        newsubfile (clojure.java.io/file subfiledst)]
    (rename-subfile-obj subfile newsubfile)))

(defn rename-subfiles [lazy-file-seq]
  "execute rename-subfile on lazy-file-obj"
  (doseq [episode-map lazy-file-seq]
    (rename-subfile episode-map)))

(defn -main
  "entry point; 1st arg is the required lookup dir path"
  [& args]
  (cond
    (nil? args) (println "usage: sub-matcher [dir]")
    :else (let [lookupdir-path (first args)
                subs (subtitle-files-with-joinkey (subtitle-files lookupdir-path))
                vids (video-files-with-joinkey (video-files lookupdir-path))
                episodes (join subs vids)
                episodes-with-subs (renamed-subtitles episodes)]
            (rename-subfiles episodes-with-subs))))
