package ie.turfclub.trainers.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "te_file_uploads", catalog = "trainers")
public class TeFile {

	
    private Long id;
    private TeTrainers fileUserId;
    private String name;
    private String thumbnailFilename;
    private String newFilename;
    private String contentType;
    private Long size;
    private Long thumbnailSize;
    private Date dateCreated;
    private Date lastUpdated;
    private Integer mergeCount;

    private String url;

    private String thumbnailUrl;

    private String deleteUrl;

    private String deleteType;
	
    private UploadType uploadType;

    public enum UploadType {
    	UPLOADED,MERGED,CONVERTED 
    }
    
    @Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "file_id", unique = true, nullable = false)
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "file_user_id", nullable = false)
	@JsonIgnore
	public TeTrainers getFileUserId() {
		return fileUserId;
	}
	public void setFileUserId(TeTrainers fileUserId) {
		this.fileUserId = fileUserId;
	}
	@Column(name = "file_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "file_thumbnail_filename")
	public String getThumbnailFilename() {
		return thumbnailFilename;
	}
	public void setThumbnailFilename(String thumbnailFilename) {
		this.thumbnailFilename = thumbnailFilename;
	}
	@Column(name = "file_new_file_name")
	public String getNewFilename() {
		return newFilename;
	}
	public void setNewFilename(String newFilename) {
		this.newFilename = newFilename;
	}
	@Column(name = "file_content_type")
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	@Column(name = "file_size")
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	@Column(name = "file_thumbnail_size")
	public Long getThumbnailSize() {
		return thumbnailSize;
	}
	public void setThumbnailSize(Long thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "file_date_created", nullable = false)
	public Date getDateCreated() {
		return dateCreated;
	}
	
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "file_last_update", nullable = false)
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
    @Transient
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    @Transient
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	 @Transient
	public String getDeleteUrl() {
		return deleteUrl;
	}
   
	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}
	 @Transient
	public String getDeleteType() {
		return deleteType;
	}
	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "file_type", nullable = false)
	public UploadType getUploadType() {
		return uploadType;
	}
	public void setUploadType(UploadType uploadType) {
		this.uploadType = uploadType;
	}
	
	@Column(name = "file_merge_count", nullable = true)
	public Integer getMergeCount() {
		return mergeCount;
	}
	public void setMergeCount(Integer mergeCount) {
		this.mergeCount = mergeCount;
	}
	
	//overridden method, has to be exactly the same like the following
		public boolean equals(Object obj) {
			if (!(obj instanceof TeFile)){
				return false;
			}
			System.out.println("File equals " + this.newFilename.equals(((TeFile) obj).newFilename));			
			return this.newFilename.equals(((TeFile) obj).newFilename);
		}
	 
		public int hashCode(){
			return newFilename.hashCode();
		}
    
}
